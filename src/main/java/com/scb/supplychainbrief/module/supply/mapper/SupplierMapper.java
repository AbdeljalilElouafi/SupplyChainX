package com.scb.supplychainbrief.module.supply.mapper;

import com.scb.supplychainbrief.module.supply.dto.SupplierDto;
import com.scb.supplychainbrief.module.supply.model.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface SupplierMapper {

    SupplierDto.SupplierResponse toSupplierResponse(Supplier supplier);

    Supplier toSupplier(SupplierDto.SupplierRequest request);


    void updateSupplierFromDto(SupplierDto.SupplierRequest request, @MappingTarget Supplier supplier);
}