package com.scb.supplychainbrief.module.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class CustomerDto {

    @Data
    public static class Request {
        @NotBlank
        private String name;
        @NotBlank
        private String address;
        @NotBlank
        private String city;
    }

    @Data
    public static class Response {
        private Long idCustomer;
        private String name;
        private String address;
        private String city;
    }
}
