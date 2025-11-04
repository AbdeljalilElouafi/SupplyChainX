package com.scb.supplychainbrief.module.supply.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

public class SupplierDto {


    @Data
    public static class SupplierRequest {
        @NotBlank(message = "Supplier name cannot be blank")
        private String name;
        private String contact;
        @PositiveOrZero(message = "Rating must be positive")
        private Double rating;
        @PositiveOrZero(message = "Lead time must be positive")
        private Integer leadTime;
    }


    @Data
    public static class SupplierResponse {
        private Long idSupplier;
        private String name;
        private String contact;
        private Double rating;
        private Integer leadTime;
    }
}