package com.scb.supplychainbrief.module.supply.service;

import com.scb.supplychainbrief.module.supply.dto.SupplyOrderDto;
import com.scb.supplychainbrief.module.supply.mapper.SupplyOrderMapper;
import com.scb.supplychainbrief.module.supply.model.RawMaterial;
import com.scb.supplychainbrief.module.supply.model.Supplier;
import com.scb.supplychainbrief.module.supply.model.SupplyOrder;
import com.scb.supplychainbrief.module.supply.repository.RawMaterialRepository;
import com.scb.supplychainbrief.module.supply.repository.SupplierRepository;
import com.scb.supplychainbrief.module.supply.repository.SupplyOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplyOrderServiceImpl implements SupplyOrderService {

    private final SupplyOrderRepository supplyOrderRepository;
    private final SupplierRepository supplierRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final SupplyOrderMapper supplyOrderMapper;

    @Override
    @Transactional
    public SupplyOrderDto.SupplyOrderResponse createSupplyOrder(SupplyOrderDto.SupplyOrderRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + request.getSupplierId()));

        List<RawMaterial> materials = rawMaterialRepository.findAllById(request.getMaterialIds());
        if (materials.size() != request.getMaterialIds().size()) {
            throw new EntityNotFoundException("One or more raw materials not found.");
        }

        SupplyOrder supplyOrder = new SupplyOrder();
        supplyOrder.setSupplier(supplier);
        supplyOrder.setMaterials(new HashSet<>(materials));
        supplyOrder.setOrderDate(request.getOrderDate());
        supplyOrder.setStatus(request.getStatus());

        SupplyOrder savedOrder = supplyOrderRepository.save(supplyOrder);
        return supplyOrderMapper.toSupplyOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplyOrderDto.SupplyOrderResponse> getAllSupplyOrders(Pageable pageable) {
        return supplyOrderRepository.findAll(pageable)
                .map(supplyOrderMapper::toSupplyOrderResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplyOrderDto.SupplyOrderResponse getSupplyOrderById(Long id) {
        SupplyOrder order = supplyOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SupplyOrder not found with id: " + id));
        return supplyOrderMapper.toSupplyOrderResponse(order);
    }

    @Override
    @Transactional
    public SupplyOrderDto.SupplyOrderResponse updateSupplyOrder(Long id, SupplyOrderDto.SupplyOrderRequest request) {
        SupplyOrder existingOrder = supplyOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SupplyOrder not found with id: " + id));

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + request.getSupplierId()));

        List<RawMaterial> materials = rawMaterialRepository.findAllById(request.getMaterialIds());
        if (materials.size() != request.getMaterialIds().size()) {
            throw new EntityNotFoundException("some raw material not found.");
        }

        existingOrder.setSupplier(supplier);
        existingOrder.setMaterials(new HashSet<>(materials));
        existingOrder.setOrderDate(request.getOrderDate());
        existingOrder.setStatus(request.getStatus());

        SupplyOrder updated = supplyOrderRepository.save(existingOrder);
        return supplyOrderMapper.toSupplyOrderResponse(updated);
    }

    @Override
    @Transactional
    public void deleteSupplyOrder(Long id) {
        SupplyOrder order = supplyOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SupplyOrder not found with id: " + id));
        supplyOrderRepository.delete(order);
    }
}
