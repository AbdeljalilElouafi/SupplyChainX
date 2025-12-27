package com.scb.supplychainbrief.module.supply.controller;

import com.scb.supplychainbrief.module.supply.dto.SupplierDto;
import com.scb.supplychainbrief.module.supply.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Tag(name = "Module 1: Approvisionnement", description = "Gestion des Fournisseurs")
public class SupplierController {

    private final SupplierService supplierService;

    @Operation(summary = "Ajouter un nouveau fournisseur ")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTIONNAIRE_APPROVISIONNEMENT')")
    public ResponseEntity<SupplierDto.SupplierResponse> createSupplier(@Valid @RequestBody SupplierDto.SupplierRequest request) {
        SupplierDto.SupplierResponse response = supplierService.createSupplier(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Consulter la liste de tous les fournisseurs ")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISEUR_LOGISTIQUE', 'RESPONSABLE_ACHATS')")
    public ResponseEntity<Page<SupplierDto.SupplierResponse>> getAllSuppliers(Pageable pageable) {

        return ResponseEntity.ok(supplierService.getAllSuppliers(pageable));
    }

    @Operation(summary = "Modifier un fournisseur existant ")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTIONNAIRE_APPROVISIONNEMENT')")
    public ResponseEntity<SupplierDto.SupplierResponse> updateSupplier(@PathVariable Long id, @Valid @RequestBody SupplierDto.SupplierRequest request) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, request));
    }

    @Operation(summary = "Supprimer un fournisseur ")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTIONNAIRE_APPROVISIONNEMENT')")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}