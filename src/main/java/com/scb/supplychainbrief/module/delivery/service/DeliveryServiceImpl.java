package com.scb.supplychainbrief.module.delivery.service;

import com.scb.supplychainbrief.module.delivery.dto.DeliveryDto;
import com.scb.supplychainbrief.module.delivery.mapper.DeliveryMapper;
import com.scb.supplychainbrief.module.delivery.model.Delivery;
import com.scb.supplychainbrief.module.delivery.model.Order;
import com.scb.supplychainbrief.module.delivery.repository.DeliveryRepository;
import com.scb.supplychainbrief.module.delivery.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public DeliveryDto.Response createDelivery(DeliveryDto.Request request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + request.getOrderId()));


        if (order.getDelivery() != null) {
            throw new IllegalStateException("Order " + order.getIdOrder() + " already has a delivery.");
        }

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setVehicle(request.getVehicle());
        delivery.setDriver(request.getDriver());
        delivery.setStatus(request.getStatus());
        delivery.setDeliveryDate(request.getDeliveryDate());
        delivery.setCost(request.getCost());


        Delivery savedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.toDeliveryResponse(savedDelivery);
    }
}
