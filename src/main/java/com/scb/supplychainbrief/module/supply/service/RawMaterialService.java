package com.scb.supplychainbrief.module.supply.service;

import com.scb.supplychainbrief.module.supply.dto.RawMaterialDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RawMaterialService {
    RawMaterialDto.RawMaterialResponse createRawMaterial(RawMaterialDto.RawMaterialRequest request);
    Page<RawMaterialDto.RawMaterialResponse> getAllRawMaterials(Pageable pageable);
    List<RawMaterialDto.RawMaterialResponse> getLowStockMaterials();
    RawMaterialDto.RawMaterialResponse updateRawMaterial(Long id, RawMaterialDto.RawMaterialRequest request);
    void deleteRawMaterial(Long id);
}
