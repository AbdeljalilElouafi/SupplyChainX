package com.scb.supplychainbrief.module.supply.controller;

import com.scb.supplychainbrief.module.supply.dto.SupplierDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SupplierControllerIT {


    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test_db")
            .withUsername("testuser")
            .withPassword("testpass");


    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.liquibase.enabled", () -> "true");
    }


    @Autowired
    private TestRestTemplate restTemplate;



    @Test
    @Order(1)
    void testCreateSupplier() {

        SupplierDto.SupplierRequest request = new SupplierDto.SupplierRequest();
        request.setName("Supplier IT");
        request.setContact("it@supplier.com");
        request.setRating(5.0);
        request.setLeadTime(10);


        ResponseEntity<SupplierDto.SupplierResponse> response = restTemplate.postForEntity(
                "/api/v1/suppliers",
                request,
                SupplierDto.SupplierResponse.class
        );


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Supplier IT", response.getBody().getName());
    }

    @Test
    @Order(2)
    void testGetAllSuppliers() {

        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/suppliers",
                String.class
        );


        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertTrue(response.getBody().contains("Supplier IT"));
    }
}