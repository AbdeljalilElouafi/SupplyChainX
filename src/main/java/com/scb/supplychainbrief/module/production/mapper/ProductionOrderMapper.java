package com.scb.supplychainbrief.module.production.mapper;

import com.scb.supplychainbrief.module.production.dto.ProductionOrderDto;
import com.scb.supplychainbrief.module.production.model.ProductionOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ProductionOrderMapper {

    @Mapping(source = "product", target = "product")
    ProductionOrderDto.Response toProductionOrderResponse(ProductionOrder order);
}
