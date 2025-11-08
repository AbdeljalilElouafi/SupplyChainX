package com.scb.supplychainbrief.module.delivery.mapper;

import com.scb.supplychainbrief.module.delivery.dto.DeliveryDto;
import com.scb.supplychainbrief.module.delivery.model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface DeliveryMapper {
    @Mapping(source = "order", target = "order")
    DeliveryDto.Response toDeliveryResponse(Delivery delivery);
}
