package com.scb.supplychainbrief.module.supply.service;

import com.scb.supplychainbrief.module.supply.dto.SupplierDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplierService {
    SupplierDto.SupplierResponse createSupplier(SupplierDto.SupplierRequest request);
    SupplierDto.SupplierResponse getSupplierById(Long id);
    Page<SupplierDto.SupplierResponse> getAllSuppliers(Pageable pageable);
    SupplierDto.SupplierResponse updateSupplier(Long id, SupplierDto.SupplierRequest request);
    void deleteSupplier(Long id);
}