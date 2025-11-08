package com.scb.supplychainbrief.module.delivery.dto;

import com.scb.supplychainbrief.common.util.OrderStatus;
import com.scb.supplychainbrief.module.production.dto.ProductDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

public class OrderDto {

    @Data
    public static class Request {
        @NotNull
        private Long customerId;
        @NotNull
        private Long productId;
        @NotNull
        @Positive
        private Integer quantity;
        @NotNull
        private OrderStatus status;
    }

    @Data
    public static class Response {
        private Long idOrder;
        private CustomerDto.Response customer;
        private ProductDto.Response product;
        private Integer quantity;
        private OrderStatus status;
        private boolean hasDelivery;
    }
}
