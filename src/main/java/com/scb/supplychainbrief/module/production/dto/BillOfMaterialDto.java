package com.scb.supplychainbrief.module.production.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

public class BillOfMaterialDto {


    // DTO for CREATING a product's BOM.

    @Data
    public static class Request {
        @NotNull
        private Long rawMaterialId;

        @NotNull
        @Positive(message = "Quantity must be greater than zero")
        private Integer quantity;
    }


    // DTO for RESPONDING with a product's BOM.

    @Data
    public static class Response {
        private Long bomEntryId;
        private Long rawMaterialId;
        private String rawMaterialName;
        private Integer quantity;
    }
}
