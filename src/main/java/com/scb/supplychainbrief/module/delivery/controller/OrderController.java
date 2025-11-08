package com.scb.supplychainbrief.module.delivery.controller;

import com.scb.supplychainbrief.module.delivery.dto.OrderDto;
import com.scb.supplychainbrief.module.delivery.service.OrderService;
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
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Module 3: Livraison", description = "Gestion des Commandes Clients")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Créer une commande client - Checks stock")
    @PostMapping
    public ResponseEntity<OrderDto.Response> createOrder(@Valid @RequestBody OrderDto.Request request) {
        return new ResponseEntity<>(orderService.createOrder(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Consulter la liste de toutes les commandes")
    @GetMapping
    public ResponseEntity<Page<OrderDto.Response>> getAllOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @Operation(summary = "Annuler une commande si non expédiée")
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
