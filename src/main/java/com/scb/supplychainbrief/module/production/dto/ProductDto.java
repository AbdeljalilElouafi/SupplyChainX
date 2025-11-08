package com.scb.supplychainbrief.module.production.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

public class ProductDto {

    @Data
    public static class Request {
        @NotBlank(message = "Product name cannot be blank")
        private String name;

        @NotNull
        @Positive
        private Integer productionTime; // in hours

        @NotNull
        @Positive
        private Double cost;

        @NotNull
        @Positive
        private Integer stock;

        @NotEmpty(message = "Bill of Materials (BOM) cannot be empty")
        @Valid
        private List<BillOfMaterialDto.Request> bomEntries;
    }

    @Data
    public static class Response {
        private Long idProduct;
        private String name;
        private Integer productionTime;
        private Double cost;
        private Integer stock;
        private List<BillOfMaterialDto.Response> bomEntries;
    }
}
