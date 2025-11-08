package com.scb.supplychainbrief.module.production.mapper;

import com.scb.supplychainbrief.module.production.dto.BillOfMaterialDto;
import com.scb.supplychainbrief.module.production.model.BillOfMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillOfMaterialMapper {

    @Mapping(source = "idBOM", target = "bomEntryId")
    @Mapping(source = "material.idMaterial", target = "rawMaterialId")
    @Mapping(source = "material.name", target = "rawMaterialName")
    BillOfMaterialDto.Response toBomResponse(BillOfMaterial bom);

}
