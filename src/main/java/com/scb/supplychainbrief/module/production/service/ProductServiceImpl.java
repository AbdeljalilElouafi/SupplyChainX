package com.scb.supplychainbrief.module.production.service;

import com.scb.supplychainbrief.module.delivery.repository.OrderRepository;
import com.scb.supplychainbrief.module.production.dto.BillOfMaterialDto;
import com.scb.supplychainbrief.module.production.dto.ProductDto;
import com.scb.supplychainbrief.module.production.mapper.ProductMapper;
import com.scb.supplychainbrief.module.production.model.BillOfMaterial;
import com.scb.supplychainbrief.module.production.model.Product;
import com.scb.supplychainbrief.module.production.repository.BillOfMaterialRepository;
import com.scb.supplychainbrief.module.production.repository.ProductRepository;
import com.scb.supplychainbrief.module.production.repository.ProductionOrderRepository;
import com.scb.supplychainbrief.module.supply.model.RawMaterial;
import com.scb.supplychainbrief.module.supply.repository.RawMaterialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BillOfMaterialRepository bomRepository;
    private final ProductMapper productMapper;
    private final RawMaterialRepository rawMaterialRepository;
    private final ProductionOrderRepository productionOrderRepository;


    private final OrderRepository orderRepository;


    @Override
    @Transactional
    public ProductDto.Response createProduct(ProductDto.Request request) {
        Product product = productMapper.toProduct(request);
        Product savedProduct = productRepository.save(product);

        for (BillOfMaterialDto.Request bomRequest : request.getBomEntries()) {
            RawMaterial material = rawMaterialRepository.findById(bomRequest.getRawMaterialId())
                    .orElseThrow(() -> new EntityNotFoundException("RawMaterial not found: " + bomRequest.getRawMaterialId()));

            BillOfMaterial bomEntry = new BillOfMaterial();
            bomEntry.setMaterial(material);
            bomEntry.setQuantity(bomRequest.getQuantity());
            savedProduct.addBomEntry(bomEntry);
        }

        Product productWithBom = productRepository.save(savedProduct);
        return productMapper.toProductResponse(productWithBom);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto.Response> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto.Response getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));


        long orderCount = productionOrderRepository.countByProduct(product);
        if (orderCount > 0) {
            throw new IllegalStateException("Cannot delete product with " + orderCount + " associated production orders.");
        }



        long clientOrderCount = orderRepository.countByProduct(product);
        if (clientOrderCount > 0) {
            throw new IllegalStateException("Cannot delete product with " + clientOrderCount + " associated customer orders.");
        }


        productRepository.delete(product);
    }
}
