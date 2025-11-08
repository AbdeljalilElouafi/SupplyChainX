package com.scb.supplychainbrief.module.production.dto;

import com.scb.supplychainbrief.common.util.ProductionOrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

public class ProductionOrderDto {

    @Data
    public static class Request {
        @NotNull
        private Long productId;

        @NotNull
        @Positive
        private Integer quantity;

        @NotNull
        private ProductionOrderStatus status; // default en_attente

        @NotNull
        private LocalDate startDate;
    }

    @Data
    public static class Response {
        private Long idOrder;
        private ProductDto.Response product;
        private Integer quantity;
        private ProductionOrderStatus status;
        private LocalDate startDate;
        private LocalDate endDate;
    }
}
