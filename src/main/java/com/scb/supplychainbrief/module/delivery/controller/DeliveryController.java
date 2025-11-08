package com.scb.supplychainbrief.module.delivery.controller;

import com.scb.supplychainbrief.module.delivery.dto.DeliveryDto;
import com.scb.supplychainbrief.module.delivery.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
@Tag(name = "Module 3: Livraison", description = "Gestion des Livraisons")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "Créer une livraison pour une commande (US40)")
    @PostMapping
    public ResponseEntity<DeliveryDto.Response> createDelivery(@Valid @RequestBody DeliveryDto.Request request) {
        return new ResponseEntity<>(deliveryService.createDelivery(request), HttpStatus.CREATED);
    }
}
