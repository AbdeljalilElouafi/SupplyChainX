package com.scb.supplychainbrief.module.supply.dto;

import com.scb.supplychainbrief.common.util.SupplyOrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class SupplyOrderDto {

    @Data
    public static class SupplyOrderRequest {
        @NotNull(message = "Supplier ID cannot be null")
        private Long supplierId;

        @NotNull(message = "Material IDs list cannot be null")
        private List<Long> materialIds;

        @NotNull(message = "Order date cannot be null")
        private LocalDate orderDate;

        @NotNull(message = "Status cannot be null")
        private SupplyOrderStatus status;
    }

    @Data
    public static class SupplyOrderResponse {
        private Long idOrder;
        private SupplierDto.SupplierResponse supplier;
        private List<RawMaterialDto.RawMaterialResponse> materials; // nested List
        private LocalDate orderDate;
        private SupplyOrderStatus status;
    }
}
