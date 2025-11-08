package com.scb.supplychainbrief.module.delivery.service;

import com.scb.supplychainbrief.common.util.OrderStatus;
import com.scb.supplychainbrief.module.delivery.dto.OrderDto;
import com.scb.supplychainbrief.module.delivery.mapper.OrderMapper;
import com.scb.supplychainbrief.module.delivery.model.Customer;
import com.scb.supplychainbrief.module.delivery.model.Order;
import com.scb.supplychainbrief.module.delivery.repository.CustomerRepository;
import com.scb.supplychainbrief.module.delivery.repository.OrderRepository;
import com.scb.supplychainbrief.module.production.model.Product;
import com.scb.supplychainbrief.module.production.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;


    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDto.Response createOrder(OrderDto.Request request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + request.getCustomerId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + request.getProductId()));


        if (product.getStock() < request.getQuantity()) {
            throw new IllegalStateException("Not enough stock for product: " + product.getName() +
                    ". Required: " + request.getQuantity() + ", Available: " + product.getStock());
        }


        product.setStock(product.getStock() - request.getQuantity());
        productRepository.save(product);


        Order order = new Order();
        order.setCustomer(customer);
        order.setProduct(product);
        order.setQuantity(request.getQuantity());
        order.setStatus(request.getStatus());

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto.Response> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toOrderResponse);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + id));

        if (order.getStatus() == OrderStatus.EN_ROUTE || order.getStatus() == OrderStatus.LIVREE) {
            throw new IllegalStateException("Cannot cancel order with status: " + order.getStatus());
        }


        Product product = order.getProduct();
        product.setStock(product.getStock() + order.getQuantity());
        productRepository.save(product);


        orderRepository.delete(order);
    }
}
