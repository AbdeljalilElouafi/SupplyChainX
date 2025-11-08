package com.scb.supplychainbrief.module.delivery.mapper;

import com.scb.supplychainbrief.module.delivery.dto.CustomerDto;
import com.scb.supplychainbrief.module.delivery.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto.Response toCustomerResponse(Customer customer);
    Customer toCustomer(CustomerDto.Request request);
}
