package com.scb.supplychainbrief.common.controller;

import com.scb.supplychainbrief.common.dto.UserDto;
import com.scb.supplychainbrief.common.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Administration", description = "Gestion des Utilisateurs")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Créer un compte utilisateur (US1)")
    @PostMapping
    public ResponseEntity<UserDto.UserResponse> createUser(@Valid @RequestBody UserDto.UserRequest request) {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Consulter la liste de tous les utilisateurs")
    @GetMapping
    public ResponseEntity<List<UserDto.UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Modifier le rôle d'un utilisateur (US2)")
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserDto.UserResponse> updateUserRole(@PathVariable Long id, @Valid @RequestBody UserDto.RoleUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUserRole(id, request.getRole()));
    }
}
