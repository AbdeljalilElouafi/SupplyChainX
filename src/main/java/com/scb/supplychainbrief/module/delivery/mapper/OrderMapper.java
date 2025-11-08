package com.scb.supplychainbrief.module.delivery.mapper;

import com.scb.supplychainbrief.module.delivery.dto.OrderDto;
import com.scb.supplychainbrief.module.delivery.model.Order;
import com.scb.supplychainbrief.module.production.mapper.ProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, ProductMapper.class})
public interface OrderMapper {

    @Mappings({
            @Mapping(source = "customer", target = "customer"),
            @Mapping(source = "product", target = "product"),
            @Mapping(source = "delivery", target = "hasDelivery")
    })
    OrderDto.Response toOrderResponse(Order order);


    default boolean mapDeliveryToBoolean(Object delivery) {
        return delivery != null;
    }
}
