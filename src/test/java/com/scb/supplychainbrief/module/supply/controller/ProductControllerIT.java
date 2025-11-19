package com.scb.supplychainbrief.module.supply.controller;

import com.scb.supplychainbrief.module.production.dto.BillOfMaterialDto;
import com.scb.supplychainbrief.module.production.dto.ProductDto;
import com.scb.supplychainbrief.module.supply.dto.RawMaterialDto;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerIT {


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


    private static Long rawMaterialId;



    @Test
    @Order(1)
    void testCreateRawMaterial_AsPrerequisite() {

        RawMaterialDto.RawMaterialRequest materialRequest = new RawMaterialDto.RawMaterialRequest();
        materialRequest.setName("Bois");
        materialRequest.setStock(100);
        materialRequest.setStockMin(10);
        materialRequest.setUnit("kg");


        ResponseEntity<RawMaterialDto.RawMaterialResponse> response = restTemplate.postForEntity(
                "/api/v1/raw-materials",
                materialRequest,
                RawMaterialDto.RawMaterialResponse.class
        );


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        rawMaterialId = response.getBody().getIdMaterial();
        assertNotNull(rawMaterialId);
    }

    @Test
    @Order(2)
    void testCreateProduct_WithBOM() {

        BillOfMaterialDto.Request bomEntry = new BillOfMaterialDto.Request();
        bomEntry.setRawMaterialId(rawMaterialId);
        bomEntry.setQuantity(5);


        ProductDto.Request productRequest = new ProductDto.Request();
        productRequest.setName("Chaise en Bois");
        productRequest.setProductionTime(3);
        productRequest.setCost(50.0);
        productRequest.setStock(0);
        productRequest.setBomEntries(List.of(bomEntry));


        ResponseEntity<ProductDto.Response> response = restTemplate.postForEntity(
                "/api/v1/products",
                productRequest,
                ProductDto.Response.class
        );


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Chaise en Bois", response.getBody().getName());
        assertEquals(1, response.getBody().getBomEntries().size());
        assertEquals(rawMaterialId, response.getBody().getBomEntries().get(0).getRawMaterialId());
    }
}