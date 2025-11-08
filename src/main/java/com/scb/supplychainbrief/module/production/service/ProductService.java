package com.scb.supplychainbrief.module.production.service;

import com.scb.supplychainbrief.module.production.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDto.Response createProduct(ProductDto.Request request);
    Page<ProductDto.Response> getAllProducts(Pageable pageable);
    ProductDto.Response getProductById(Long id);
    // TODO: Add Update
    void deleteProduct(Long id);
}
