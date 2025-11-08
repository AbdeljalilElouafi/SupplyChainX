package com.scb.supplychainbrief.module.delivery.service;

import com.scb.supplychainbrief.module.delivery.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto.Response createOrder(OrderDto.Request request);
    Page<OrderDto.Response> getAllOrders(Pageable pageable);
    void cancelOrder(Long id);
}
