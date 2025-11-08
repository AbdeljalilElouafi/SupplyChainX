package com.scb.supplychainbrief.module.production.mapper;

import com.scb.supplychainbrief.module.production.dto.ProductDto;
import com.scb.supplychainbrief.module.production.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BillOfMaterialMapper.class})
public interface ProductMapper {


    @Mapping(source = "bomEntries", target = "bomEntries")
    ProductDto.Response toProductResponse(Product product);


    @Mapping(target = "bomEntries", ignore = true)
    @Mapping(target = "productionOrders", ignore = true)
    @Mapping(target = "idProduct", ignore = true)
    Product toProduct(ProductDto.Request request);
}
