package com.scb.supplychainbrief.module.production.service;

import com.scb.supplychainbrief.module.production.dto.ProductionOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductionOrderService {
    ProductionOrderDto.Response createProductionOrder(ProductionOrderDto.Request request);
    Page<ProductionOrderDto.Response> getAllProductionOrders(Pageable pageable);
    void cancelProductionOrder(Long id);
    // TODO: Add Update
}
