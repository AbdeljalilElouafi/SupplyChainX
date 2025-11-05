package com.scb.supplychainbrief.module.supply.service;

import com.scb.supplychainbrief.common.util.SupplyOrderStatus;
import com.scb.supplychainbrief.module.supply.dto.SupplierDto;
import com.scb.supplychainbrief.module.supply.mapper.SupplierMapper;
import com.scb.supplychainbrief.module.supply.model.Supplier;
import com.scb.supplychainbrief.module.supply.repository.SupplierRepository;
import com.scb.supplychainbrief.module.supply.repository.SupplyOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor // this to automatically inject final att in th constructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
     private final SupplyOrderRepository supplyOrderRepository;

    @Override
    public SupplierDto.SupplierResponse createSupplier(SupplierDto.SupplierRequest request) {
        Supplier supplier = supplierMapper.toSupplier(request);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toSupplierResponse(savedSupplier);
    }

    @Override
    public SupplierDto.SupplierResponse getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
        return supplierMapper.toSupplierResponse(supplier);
    }

    @Override
    public Page<SupplierDto.SupplierResponse> getAllSuppliers(Pageable pageable) {
        return supplierRepository.findAll(pageable)
                .map(supplierMapper::toSupplierResponse);
    }

    @Override
    public SupplierDto.SupplierResponse updateSupplier(Long id, SupplierDto.SupplierRequest request) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));

        supplierMapper.updateSupplierFromDto(request, existingSupplier);

        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        return supplierMapper.toSupplierResponse(updatedSupplier);
    }

    @Override
    public void deleteSupplier(Long id) {



        long activeOrders = supplyOrderRepository.countBySupplierIdSupplierAndStatusNot(id, SupplyOrderStatus.RECUE);
        if (activeOrders > 0) {
            throw new IllegalStateException("Cannot delete supplier with " + activeOrders + " active orders.");
        }


        if (!supplierRepository.existsById(id)) {
            throw new EntityNotFoundException("Supplier not found with id: " + id);
        }


        // TODO: We should also disconnect from RawMaterials
//        Supplier supplier = supplierRepository.findById(id).get();
//        supplier.getRawMaterials().clear();
//        supplierRepository.save(supplier);
//
//        supplierRepository.deleteById(id);
    }
}