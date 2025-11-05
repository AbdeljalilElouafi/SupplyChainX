package com.scb.supplychainbrief.module.supply.service;

import com.scb.supplychainbrief.module.supply.dto.SupplyOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplyOrderService {
    SupplyOrderDto.SupplyOrderResponse createSupplyOrder(SupplyOrderDto.SupplyOrderRequest request);
    Page<SupplyOrderDto.SupplyOrderResponse> getAllSupplyOrders(Pageable pageable);

    SupplyOrderDto.SupplyOrderResponse getSupplyOrderById(Long id);

    SupplyOrderDto.SupplyOrderResponse updateSupplyOrder(Long id, SupplyOrderDto.SupplyOrderRequest request);

    void deleteSupplyOrder(Long id);
}
