package com.scb.supplychainbrief.module.delivery.service;

import com.scb.supplychainbrief.module.delivery.dto.CustomerDto;
import com.scb.supplychainbrief.module.delivery.mapper.CustomerMapper;
import com.scb.supplychainbrief.module.delivery.model.Customer;
import com.scb.supplychainbrief.module.delivery.repository.CustomerRepository;
import com.scb.supplychainbrief.module.delivery.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final OrderRepository orderRepository;

    @Override
    public CustomerDto.Response createCustomer(CustomerDto.Request request) {
        Customer customer = customerMapper.toCustomer(request);
        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }

    @Override
    public Page<CustomerDto.Response> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(customerMapper::toCustomerResponse);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));

        long orderCount = orderRepository.countByCustomer(customer);
        if (orderCount > 0) {
            throw new IllegalStateException("Cannot delete customer with " + orderCount + " associated orders.");
        }

        customerRepository.delete(customer);
    }
}
