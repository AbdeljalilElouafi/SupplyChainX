package com.scb.supplychainbrief.common.controller;

import com.scb.supplychainbrief.common.dto.AuthDto;
import com.scb.supplychainbrief.common.model.User;
import com.scb.supplychainbrief.common.service.JwtService;
import com.scb.supplychainbrief.common.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Sécurité", description = "Authentification et gestion des Tokens")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @Operation(summary = "Authentification (Login)")
    @PostMapping("/login")
    public ResponseEntity<AuthDto.TokenResponse> login(@Valid @RequestBody AuthDto.LoginRequest request) {

        // 1. Authentification Spring Security (vérifie le mot de passe)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();

        // 2. Génération des tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // 3. Sauvegarde du Refresh Token en base de données
        userService.updateRefreshToken(user, refreshToken);

        return ResponseEntity.ok(new AuthDto.TokenResponse(accessToken, refreshToken));
    }

    @Operation(summary = "Renouvellement du token via Refresh Token")
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthDto.TokenResponse> refreshToken(@RequestBody AuthDto.RefreshTokenRequest request) {
        try {
            // 1. Extraire l'utilisateur du refresh token
            String userEmail = jwtService.extractUsername(request.getRefreshToken());
            User user = (User) userService.loadUserByUsername(userEmail);

            // 2. Validation du token (expiration, validité) et vérification de la rotation
            if (jwtService.isTokenValid(request.getRefreshToken(), user) &&
                    request.getRefreshToken().equals(user.getRefreshToken()))
            {
                // Rotation obligatoire: Générer un nouveau refresh token
                String newAccessToken = jwtService.generateAccessToken(user);
                String newRefreshToken = jwtService.generateRefreshToken(user);

                // 3. Révocation de l'ancien token et sauvegarde du nouveau
                userService.updateRefreshToken(user, newRefreshToken);

                return ResponseEntity.ok(new AuthDto.TokenResponse(newAccessToken, newRefreshToken));
            }
        } catch (Exception e) {
            // Jeton invalide ou utilisateur non trouvé
            return new ResponseEntity("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity("Invalid refresh token", HttpStatus.UNAUTHORIZED);
    }

    // Déconnexion (Révoque le refresh token)
    @Operation(summary = "Déconnexion (révoque le Refresh Token)")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody AuthDto.RefreshTokenRequest request) {
        try {
            String userEmail = jwtService.extractUsername(request.getRefreshToken());
            User user = (User) userService.loadUserByUsername(userEmail);

            // Si le token correspond, révoquer (le mettre à null)
            if (request.getRefreshToken().equals(user.getRefreshToken())) {
                userService.updateRefreshToken(user, null);
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            // Ignorer si l'utilisateur ou le token n'existe pas
        }
        return ResponseEntity.noContent().build();
    }
}