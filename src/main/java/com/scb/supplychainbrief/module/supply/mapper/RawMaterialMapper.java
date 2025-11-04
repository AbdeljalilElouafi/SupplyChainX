package com.scb.supplychainbrief.module.supply.mapper;

import com.scb.supplychainbrief.module.supply.dto.RawMaterialDto;
import com.scb.supplychainbrief.module.supply.model.RawMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RawMaterialMapper {

    RawMaterialDto.RawMaterialResponse toRawMaterialResponse(RawMaterial rawMaterial);

    RawMaterial toRawMaterial(RawMaterialDto.RawMaterialRequest request);

    void updateRawMaterialFromDto(RawMaterialDto.RawMaterialRequest request, @MappingTarget RawMaterial rawMaterial);
}
