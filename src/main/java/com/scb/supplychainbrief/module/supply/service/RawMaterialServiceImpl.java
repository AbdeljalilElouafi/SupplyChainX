package com.scb.supplychainbrief.module.supply.service;

import com.scb.supplychainbrief.module.supply.dto.RawMaterialDto;
import com.scb.supplychainbrief.module.supply.mapper.RawMaterialMapper;
import com.scb.supplychainbrief.module.supply.model.RawMaterial;
import com.scb.supplychainbrief.module.supply.repository.RawMaterialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RawMaterialServiceImpl implements RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;
    private final RawMaterialMapper rawMaterialMapper;


    @Override
    public RawMaterialDto.RawMaterialResponse createRawMaterial(RawMaterialDto.RawMaterialRequest request) {
        RawMaterial rawMaterial = rawMaterialMapper.toRawMaterial(request);
        RawMaterial savedMaterial = rawMaterialRepository.save(rawMaterial);
        return rawMaterialMapper.toRawMaterialResponse(savedMaterial);
    }

    @Override
    public Page<RawMaterialDto.RawMaterialResponse> getAllRawMaterials(Pageable pageable) {
        return rawMaterialRepository.findAll(pageable)
                .map(rawMaterialMapper::toRawMaterialResponse);
    }

    @Override
    public List<RawMaterialDto.RawMaterialResponse> getLowStockMaterials() {

        return rawMaterialRepository.findLowStockMaterials().stream()
                .map(rawMaterialMapper::toRawMaterialResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RawMaterialDto.RawMaterialResponse updateRawMaterial(Long id, RawMaterialDto.RawMaterialRequest request) {
        RawMaterial existingMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RawMaterial not found with id: " + id));

        rawMaterialMapper.updateRawMaterialFromDto(request, existingMaterial);
        RawMaterial updatedMaterial = rawMaterialRepository.save(existingMaterial);
        return rawMaterialMapper.toRawMaterialResponse(updatedMaterial);
    }


    @Override
    public void deleteRawMaterial(Long id) {


        RawMaterial material = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RawMaterial not found with id:" + id));


        if (!material.getSupplyOrders().isEmpty()) {
            throw new IllegalStateException("Cannot delete raw material, it is used in active supply orders.");
        }


        try {
            rawMaterialRepository.delete(material);
            rawMaterialRepository.flush();
        } catch (DataIntegrityViolationException e) {

            throw new IllegalStateException("Cannot delete raw material, it is used in a Product's Bill of Materials.", e);
        }
    }
}
