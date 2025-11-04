package com.scb.supplychainbrief.module.supply.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

public class RawMaterialDto {

    @Data
    public static class RawMaterialRequest {
        @NotBlank(message = "Material name cannot be blank")
        private String name;

        @NotNull(message = "Stock cannot be null")
        @PositiveOrZero(message = "Stock must be positive or zero")
        private Integer stock;

        @NotNull(message = "Minimum stock cannot be null")
        @PositiveOrZero(message = "Minimum stock must be positive or zero")
        private Integer stockMin;

        private String unit;
    }

    @Data
    public static class RawMaterialResponse {
        private Long idMaterial;
        private String name;
        private Integer stock;
        private Integer stockMin;
        private String unit;
    }
}
