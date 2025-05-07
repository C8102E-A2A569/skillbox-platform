package com.skillbox.controller;

import com.skillbox.dto.auth.ExtendedRegisterRequest;
import com.skillbox.dto.auth.AuthenticationResponse;
import com.skillbox.service.ExtendedAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@Tag(name = "Управление пользователями", description = "API для управления пользователями с назначением ролей")
public class ExtendedAuthenticationController {

    private final ExtendedAuthenticationService service;

    @Operation(
            summary = "Регистрация пользователя с ролями",
            description = "Создает нового пользователя с указанными ролями (требуется право MANAGE_ROLES)"
    )
    @PostMapping("/register")
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    public ResponseEntity<AuthenticationResponse> registerWithRoles(
            @RequestBody ExtendedRegisterRequest request
    ) {
        return ResponseEntity.ok(service.registerWithRoles(request));
    }
}