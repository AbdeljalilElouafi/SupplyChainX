package com.scb.supplychainbrief.module.production.controller;

import com.scb.supplychainbrief.common.util.ProductionOrderStatus;
import com.scb.supplychainbrief.module.production.dto.BillOfMaterialDto;
import com.scb.supplychainbrief.module.production.dto.ProductDto;
import com.scb.supplychainbrief.module.production.dto.ProductionOrderDto;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductionOrderBusinessRuleIT {


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


    private static Long materialId_Acier;
    private static Long productId_Table;

    @Test
    @Order(1)
    void testCreatePrerequisites() {

        RawMaterialDto.RawMaterialRequest materialRequest = new RawMaterialDto.RawMaterialRequest();
        materialRequest.setName("Acier");
        materialRequest.setStock(10);
        materialRequest.setStockMin(5);
        materialRequest.setUnit("kg");

        ResponseEntity<RawMaterialDto.RawMaterialResponse> matResponse = restTemplate.postForEntity(
                "/api/v1/raw-materials", materialRequest, RawMaterialDto.RawMaterialResponse.class
        );
        assertEquals(HttpStatus.CREATED, matResponse.getStatusCode());
        materialId_Acier = matResponse.getBody().getIdMaterial();


        BillOfMaterialDto.Request bomEntry = new BillOfMaterialDto.Request();
        bomEntry.setRawMaterialId(materialId_Acier);
        bomEntry.setQuantity(20);

        ProductDto.Request productRequest = new ProductDto.Request();
        productRequest.setName("Table en Acier");
        productRequest.setStock(0);
        productRequest.setBomEntries(List.of(bomEntry));

        ResponseEntity<ProductDto.Response> prodResponse = restTemplate.postForEntity(
                "/api/v1/products", productRequest, ProductDto.Response.class
        );
        assertEquals(HttpStatus.CREATED, prodResponse.getStatusCode());
        productId_Table = prodResponse.getBody().getIdProduct();
    }


    @Test
    @Order(2)
    void testCreateProductionOrder_ShouldFail_DueToInsufficientStock() {

        ProductionOrderDto.Request orderRequest = new ProductionOrderDto.Request();
        orderRequest.setProductId(productId_Table);
        orderRequest.setQuantity(1);
        orderRequest.setStatus(ProductionOrderStatus.EN_ATTENTE);
        orderRequest.setStartDate(LocalDate.now());


        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/v1/production-orders/check-and-create",
                orderRequest,
                String.class
        );


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());


        String errorBody = response.getBody();
        assertNotNull(errorBody);
        assertTrue(errorBody.contains("Stock insuffisant pour Acier"));
        assertTrue(errorBody.contains("Requis: 20, Disponible: 10"));
    }
}