package com.scb.supplychainbrief.module.production.controller;

import com.scb.supplychainbrief.module.production.dto.ProductionOrderDto;
import com.scb.supplychainbrief.module.production.service.ProductionOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/production-orders")
@RequiredArgsConstructor
@Tag(name = "Module 2: Production", description = "Gestion des Ordres de Production")
public class ProductionOrderController {

    private final ProductionOrderService productionOrderService;

    @Operation(summary = "Créer un ordre de production (US23) - Checks stock (US28)")
    @PostMapping
    public ResponseEntity<ProductionOrderDto.Response> createProductionOrder(@Valid @RequestBody ProductionOrderDto.Request request) {
        return new ResponseEntity<>(productionOrderService.createProductionOrder(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Consulter la liste de tous les ordres (US26)")
    @GetMapping
    public ResponseEntity<Page<ProductionOrderDto.Response>> getAllProductionOrders(Pageable pageable) {
        return ResponseEntity.ok(productionOrderService.getAllProductionOrders(pageable));
    }

    @Operation(summary = "Annuler un ordre si non commencé (US25)")
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelProductionOrder(@PathVariable Long id) {
        productionOrderService.cancelProductionOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductionOrderDto.Response> updateProductionOrder(@PathVariable Long id) {
        return new ResponseEntity<>(productionOrderService.updateProductionOrder(id), HttpStatus.OK);
    }

}
