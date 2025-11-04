package com.scb.supplychainbrief.module.supply.controller;

import com.scb.supplychainbrief.module.supply.dto.RawMaterialDto;
import com.scb.supplychainbrief.module.supply.service.RawMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/raw-materials")
@RequiredArgsConstructor
@Tag(name = "Module 1: Approvisionnement", description = "Gestion des Matières Premières")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @Operation(summary = "Ajouter une nouvelle matière première (US8)")
    @PostMapping
    public ResponseEntity<RawMaterialDto.RawMaterialResponse> createRawMaterial(@Valid @RequestBody RawMaterialDto.RawMaterialRequest request) {
        return new ResponseEntity<>(rawMaterialService.createRawMaterial(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Consulter la liste de toutes les matières premières (US11)")
    @GetMapping
    public ResponseEntity<Page<RawMaterialDto.RawMaterialResponse>> getAllRawMaterials(Pageable pageable) {
        return ResponseEntity.ok(rawMaterialService.getAllRawMaterials(pageable));
    }

    @Operation(summary = "Filtrer les matières en stock critique (US12)")
    @GetMapping("/low-stock")
    public ResponseEntity<List<RawMaterialDto.RawMaterialResponse>> getLowStockMaterials() {
        return ResponseEntity.ok(rawMaterialService.getLowStockMaterials());
    }

    @Operation(summary = "Modifier une matière première existante (US9)")
    @PutMapping("/{id}")
    public ResponseEntity<RawMaterialDto.RawMaterialResponse> updateRawMaterial(@PathVariable Long id, @Valid @RequestBody RawMaterialDto.RawMaterialRequest request) {
        return ResponseEntity.ok(rawMaterialService.updateRawMaterial(id, request));
    }

    @Operation(summary = "Supprimer une matière première (US10)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRawMaterial(@PathVariable Long id) {
        rawMaterialService.deleteRawMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
