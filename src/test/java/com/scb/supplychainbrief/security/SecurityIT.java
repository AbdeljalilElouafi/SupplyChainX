package com.scb.supplychainbrief.security;

import com.scb.supplychainbrief.common.dto.AuthDto;
import com.scb.supplychainbrief.common.dto.UserDto;
import com.scb.supplychainbrief.common.util.Role;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecurityIT {

    // --- Configuration de la DB de Test ---
    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.liquibase.enabled", () -> "true");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    // Variables partagées entre les étapes du test
    private static String adminAccessToken;
    private static String adminRefreshToken;
    private static String userAccessToken;

    // --- SCÉNARIOS DE TEST ---

    @Test
    @Order(1)
    @DisplayName("1. Création des comptes (Public)")
    void createAccounts() {
        // Créer un Admin
        UserDto.UserRequest adminReq = new UserDto.UserRequest();
        adminReq.setFirstName("Admin");
        adminReq.setLastName("System");
        adminReq.setEmail("admin@test.com");
        adminReq.setPassword("password123");
        adminReq.setRole(Role.ADMIN);

        ResponseEntity<UserDto.UserResponse> resAdmin = restTemplate.postForEntity("/api/v1/users", adminReq, UserDto.UserResponse.class);
        assertEquals(HttpStatus.CREATED, resAdmin.getStatusCode());


        UserDto.UserRequest userReq = new UserDto.UserRequest();
        userReq.setFirstName("User");
        userReq.setLastName("Prod");
        userReq.setEmail("user@test.com");
        userReq.setPassword("password123");
        userReq.setRole(Role.SUPERVISEUR_PRODUCTION);

        ResponseEntity<UserDto.UserResponse> resUser = restTemplate.postForEntity("/api/v1/users", userReq, UserDto.UserResponse.class);
        assertEquals(HttpStatus.CREATED, resUser.getStatusCode());
    }

    @Test
    @Order(2)
    @DisplayName("2. Login (Valide & Invalide)")
    void testLogin() {
        // Login Invalide (Mauvais mot de passe)
        AuthDto.LoginRequest badReq = new AuthDto.LoginRequest();
        badReq.setEmail("admin@test.com");
        badReq.setPassword("wrong_pass");

        ResponseEntity<AuthDto.TokenResponse> resFail = restTemplate.postForEntity("/api/v1/auth/login", badReq, AuthDto.TokenResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED, resFail.getStatusCode());

        // Login Valide
        AuthDto.LoginRequest goodReq = new AuthDto.LoginRequest();
        goodReq.setEmail("admin@test.com");
        goodReq.setPassword("password123");

        ResponseEntity<AuthDto.TokenResponse> resOk = restTemplate.postForEntity("/api/v1/auth/login", goodReq, AuthDto.TokenResponse.class);
        assertEquals(HttpStatus.OK, resOk.getStatusCode());
        assertNotNull(resOk.getBody().getAccessToken());

        adminAccessToken = resOk.getBody().getAccessToken();
        adminRefreshToken = resOk.getBody().getRefreshToken();

        // Login pour l'utilisateur restreint
        AuthDto.LoginRequest userLogin = new AuthDto.LoginRequest();
        userLogin.setEmail("user@test.com");
        userLogin.setPassword("password123");
        userAccessToken = restTemplate.postForEntity("/api/v1/auth/login", userLogin, AuthDto.TokenResponse.class).getBody().getAccessToken();
    }

    @Test
    @Order(3)
    @DisplayName("3. Accès sans Token (Doit échouer)")
    void testAccessWithoutToken() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/suppliers", String.class);
        // Spring Security retourne 403 Forbidden par défaut pour les accès non autorisés sur des routes protégées
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(4)
    @DisplayName("4. Autorisation par Rôle (RBAC)")
    void testAuthorization() {
        // L'utilisateur PRODUCTION tente d'accéder à la liste des utilisateurs (ADMIN ONLY)
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userAccessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/users/all", HttpMethod.GET, entity, String.class);

        // Doit être interdit (403) car le rôle GESTIONNAIRE_PRODUCTION n'est pas ADMIN
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        // L'ADMIN tente d'y accéder
        headers.setBearerAuth(adminAccessToken);
        entity = new HttpEntity<>(headers);
        ResponseEntity<Object[]> adminResponse = restTemplate.exchange("/api/v1/users", HttpMethod.GET, entity, Object[].class);

        assertEquals(HttpStatus.OK, adminResponse.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("5. Refresh Token & Rotation")
    void testRefreshToken() {
        AuthDto.RefreshTokenRequest refreshReq = new AuthDto.RefreshTokenRequest();
        refreshReq.setRefreshToken(adminRefreshToken);

        // ACT: Renouveler le token
        ResponseEntity<AuthDto.TokenResponse> response = restTemplate.postForEntity("/api/v1/auth/refresh-token", refreshReq, AuthDto.TokenResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        String newAccessToken = response.getBody().getAccessToken();
        String newRefreshToken = response.getBody().getRefreshToken();

        assertNotEquals(adminAccessToken, newAccessToken);
        assertNotEquals(adminRefreshToken, newRefreshToken); // Vérifie la rotation

        // ACT 2: Tenter d'utiliser l'ANCIEN refresh token (doit échouer car révoqué/tourné)
        ResponseEntity<String> failResponse = restTemplate.postForEntity("/api/v1/auth/refresh-token", refreshReq, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, failResponse.getStatusCode());
    }

    @Test
    @Order(6)
    @DisplayName("6. Logout (Révocation)")
    void testLogout() {
        // On récupère un nouveau refresh token via login
        AuthDto.LoginRequest login = new AuthDto.LoginRequest();
        login.setEmail("admin@test.com");
        login.setPassword("password123");
        String refreshToken = restTemplate.postForEntity("/api/v1/auth/login", login, AuthDto.TokenResponse.class).getBody().getRefreshToken();

        // Logout
        AuthDto.RefreshTokenRequest logoutReq = new AuthDto.RefreshTokenRequest();
        logoutReq.setRefreshToken(refreshToken);
        ResponseEntity<Void> logoutRes = restTemplate.postForEntity("/api/v1/auth/logout", logoutReq, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, logoutRes.getStatusCode());

        // Tenter de refresh après logout
        ResponseEntity<String> refreshRes = restTemplate.postForEntity("/api/v1/auth/refresh-token", logoutReq, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, refreshRes.getStatusCode());
    }
}