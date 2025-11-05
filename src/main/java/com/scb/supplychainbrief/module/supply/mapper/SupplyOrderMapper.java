package com.scb.supplychainbrief.module.supply.mapper;

import com.scb.supplychainbrief.module.supply.dto.SupplyOrderDto;
import com.scb.supplychainbrief.module.supply.model.SupplyOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SupplierMapper.class, RawMaterialMapper.class})
public interface SupplyOrderMapper {

    // MapStruct katkellef b SupplierMapper ou RawMaterialMapper
    @Mapping(source = "supplier", target = "supplier")
    @Mapping(source = "materials", target = "materials")
    SupplyOrderDto.SupplyOrderResponse toSupplyOrderResponse(SupplyOrder supplyOrder);

}
