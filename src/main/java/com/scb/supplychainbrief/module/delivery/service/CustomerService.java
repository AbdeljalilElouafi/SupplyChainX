package com.scb.supplychainbrief.module.delivery.service;

import com.scb.supplychainbrief.module.delivery.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerDto.Response createCustomer(CustomerDto.Request request);
    Page<CustomerDto.Response> getAllCustomers(Pageable pageable);
    void deleteCustomer(Long id);

}
