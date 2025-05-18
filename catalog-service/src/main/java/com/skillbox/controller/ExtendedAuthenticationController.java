package com.skillbox.controller;

import com.skillbox.dto.auth.ExtendedRegisterRequest;
import com.skillbox.dto.auth.AuthenticationResponse;
import com.skillbox.repository.sql.RoleRepository;
import com.skillbox.repository.sql.UserAccountRepository;
import com.skillbox.service.ExtendedAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final RoleRepository roleRepository;
    private final UserAccountRepository userAccountRepository;

    @Operation(
            summary = "Регистрация пользователя с ролями",
            description = "Создает нового пользователя с указанными ролями (требуется право MANAGE_ROLES, кроме первого администратора)"
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerWithRoles(
            @RequestBody ExtendedRegisterRequest request
    ) {
        // Проверяем, есть ли в системе пользователи с ролью ROLE_ADMIN
        boolean hasAdmin = roleRepository.findByName("ROLE_ADMIN")
                .map(role -> userAccountRepository.countByRolesContaining(role) > 0)
                .orElse(false);

        // Если уже есть админ — требуем привилегию MANAGE_ROLES
        if (hasAdmin) {
            // Метод ниже требует, чтобы у вызывающего была MANAGE_ROLES
            return withManageRolesCheck(request);
        }

        // Первый пользователь — разрешаем создать без MANAGE_ROLES
        return ResponseEntity.ok(service.registerWithRoles(request));
    }

    public ResponseEntity<AuthenticationResponse> withManageRolesCheck(ExtendedRegisterRequest request) {
        return ResponseEntity.ok(service.registerWithRoles(request));
    }

}