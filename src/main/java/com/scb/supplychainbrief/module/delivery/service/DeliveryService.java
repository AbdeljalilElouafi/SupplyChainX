package com.scb.supplychainbrief.module.delivery.service;

import com.scb.supplychainbrief.module.delivery.dto.DeliveryDto;

public interface DeliveryService {
    DeliveryDto.Response createDelivery(DeliveryDto.Request request);
}
