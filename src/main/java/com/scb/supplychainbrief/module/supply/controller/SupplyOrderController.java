package com.scb.supplychainbrief.module.supply.controller;

import com.scb.supplychainbrief.module.supply.dto.SupplyOrderDto;
import com.scb.supplychainbrief.module.supply.service.SupplyOrderService;
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
@RequestMapping("/api/v1/supply-orders")
@RequiredArgsConstructor
@Tag(name = "Module 1: Approvisionnement", description = "Gestion des Commandes d'Approvisionnement")
public class SupplyOrderController {

    private final SupplyOrderService supplyOrderService;

    @Operation(summary = "Créer une commande d'approvisionnement (US13)")
    @PostMapping
    public ResponseEntity<SupplyOrderDto.SupplyOrderResponse> createSupplyOrder(@Valid @RequestBody SupplyOrderDto.SupplyOrderRequest request) {
        return new ResponseEntity<>(supplyOrderService.createSupplyOrder(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Consulter la liste de toutes les commandes (US16)")
    @GetMapping
    public ResponseEntity<Page<SupplyOrderDto.SupplyOrderResponse>> getAllSupplyOrders(Pageable pageable) {
        return ResponseEntity.ok(supplyOrderService.getAllSupplyOrders(pageable));
    }

    @Operation(summary = "Consulter une commande par ID")
    @GetMapping("/{id}")
    public ResponseEntity<SupplyOrderDto.SupplyOrderResponse> getSupplyOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(supplyOrderService.getSupplyOrderById(id));
    }

    @Operation(summary = "Mettre à jour une commande d'approvisionnement")
    @PutMapping("/{id}")
    public ResponseEntity<SupplyOrderDto.SupplyOrderResponse> updateSupplyOrder(
            @PathVariable Long id,
            @Valid @RequestBody SupplyOrderDto.SupplyOrderRequest request) {
        return ResponseEntity.ok(supplyOrderService.updateSupplyOrder(id, request));
    }

    @Operation(summary = "Supprimer une commande d'approvisionnement")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplyOrder(@PathVariable Long id) {
        supplyOrderService.deleteSupplyOrder(id);
        return ResponseEntity.noContent().build();
    }
}
