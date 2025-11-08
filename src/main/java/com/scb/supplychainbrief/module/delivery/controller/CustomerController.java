package com.scb.supplychainbrief.module.delivery.controller;

import com.scb.supplychainbrief.module.delivery.dto.CustomerDto;
import com.scb.supplychainbrief.module.delivery.service.CustomerService;
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
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Module 3: Livraison", description = "Gestion des Clients")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Ajouter un client ")
    @PostMapping
    public ResponseEntity<CustomerDto.Response> createCustomer(@Valid @RequestBody CustomerDto.Request request) {
        return new ResponseEntity<>(customerService.createCustomer(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Consulter la liste de tous les clients ")
    @GetMapping
    public ResponseEntity<Page<CustomerDto.Response>> getAllCustomers(Pageable pageable) {
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }

    @Operation(summary = "Supprimer un client ")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
