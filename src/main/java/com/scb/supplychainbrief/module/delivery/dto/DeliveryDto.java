package com.scb.supplychainbrief.module.delivery.dto;

import com.scb.supplychainbrief.common.util.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

public class DeliveryDto {

    @Data
    public static class Request {
        @NotNull
        private Long orderId;
        private String vehicle;
        private String driver;
        @NotNull
        private DeliveryStatus status;
        @NotNull
        private LocalDate deliveryDate;
        @NotNull
        @Positive
        private Double cost;
    }

    @Data
    public static class Response {
        private Long idDelivery;
        private OrderDto.Response order;
        private String vehicle;
        private String driver;
        private DeliveryStatus status;
        private LocalDate deliveryDate;
        private Double cost;
    }
}
