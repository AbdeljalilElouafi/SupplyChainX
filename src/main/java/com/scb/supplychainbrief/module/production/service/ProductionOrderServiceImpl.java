package com.scb.supplychainbrief.module.production.service;

import com.scb.supplychainbrief.common.util.ProductionOrderStatus;
import com.scb.supplychainbrief.module.production.dto.ProductionOrderDto;
import com.scb.supplychainbrief.module.production.mapper.ProductionOrderMapper;
import com.scb.supplychainbrief.module.production.model.BillOfMaterial;
import com.scb.supplychainbrief.module.production.model.Product;
import com.scb.supplychainbrief.module.production.model.ProductionOrder;
import com.scb.supplychainbrief.module.production.repository.BillOfMaterialRepository;
import com.scb.supplychainbrief.module.production.repository.ProductRepository;
import com.scb.supplychainbrief.module.production.repository.ProductionOrderRepository;
import com.scb.supplychainbrief.module.supply.model.RawMaterial;
import com.scb.supplychainbrief.module.supply.repository.RawMaterialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductionOrderServiceImpl implements ProductionOrderService {

    private final ProductionOrderRepository productionOrderRepository;
    private final ProductRepository productRepository;
    private final BillOfMaterialRepository bomRepository;
    private final ProductionOrderMapper productionOrderMapper;

    private final RawMaterialRepository rawMaterialRepository;

    @Override
    @Transactional
    public ProductionOrderDto.Response createProductionOrder(ProductionOrderDto.Request request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + request.getProductId()));

        List<BillOfMaterial> bom = bomRepository.findByProduct(product);
        if (bom.isEmpty()) {
            throw new IllegalStateException("Product has no Bill of Materials. Cannot create production order.");
        }

        for (BillOfMaterial bomEntry : bom) {
            RawMaterial material = bomEntry.getMaterial();
            int requiredQuantity = bomEntry.getQuantity() * request.getQuantity();

            if (material.getStock() < requiredQuantity) {
                throw new IllegalStateException("Not enough stock for: " + material.getName() +
                        ". Required: " + requiredQuantity + ", Available: " + material.getStock());
            }
        }

        for (BillOfMaterial bomEntry : bom) {
            RawMaterial material = bomEntry.getMaterial();
            int requiredQuantity = bomEntry.getQuantity() * request.getQuantity();
            material.setStock(material.getStock() - requiredQuantity);
            rawMaterialRepository.save(material);
        }

        long productionDays = product.getProductionTime() / 24;
        LocalDate endDate = request.getStartDate().plus(productionDays, ChronoUnit.DAYS);

        ProductionOrder order = new ProductionOrder();
        order.setProduct(product);
        order.setQuantity(request.getQuantity());
        order.setStatus(request.getStatus());
        order.setStartDate(request.getStartDate());
        order.setEndDate(endDate);

        ProductionOrder savedOrder = productionOrderRepository.save(order);
        return productionOrderMapper.toProductionOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductionOrderDto.Response> getAllProductionOrders(Pageable pageable) {
        return productionOrderRepository.findAll(pageable)
                .map(productionOrderMapper::toProductionOrderResponse);
    }

    @Override
    @Transactional
    public void cancelProductionOrder(Long id) {
        ProductionOrder order = productionOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductionOrder not found: " + id));

        if (order.getStatus() != ProductionOrderStatus.EN_ATTENTE) {
            throw new IllegalStateException("Cannot cancel order that is already in status: " + order.getStatus());
        }


        productionOrderRepository.delete(order);
    }
}
