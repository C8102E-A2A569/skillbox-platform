package com.skillbox.controller;

import com.skillbox.security.entity.Privilege;
import com.skillbox.security.entity.Role;
import com.skillbox.repository.sql.PrivilegeRepository;
import com.skillbox.repository.sql.RoleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Администрирование", description = "API для управления ролями и привилегиями")
public class AdminController {

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    @Operation(
            summary = "Создать привилегию",
            description = "Создает новую привилегию в системе"
    )
    @PostMapping("/privileges")
    public ResponseEntity<Privilege> createPrivilege(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        Privilege privilege = Privilege.builder().name(name).build();
        return ResponseEntity.ok(privilegeRepository.save(privilege));
    }

    @Operation(
            summary = "Получить все привилегии",
            description = "Возвращает список всех привилегий в системе"
    )
    @GetMapping("/privileges")
    public ResponseEntity<List<Privilege>> getAllPrivileges() {
        return ResponseEntity.ok(privilegeRepository.findAll());
    }

    @Operation(
            summary = "Создать роль",
            description = "Создает новую роль в системе"
    )
    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        Role role = Role.builder().name(name).build();
        return ResponseEntity.ok(roleRepository.save(role));
    }

    @Operation(
            summary = "Получить все роли",
            description = "Возвращает список всех ролей в системе"
    )
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @Operation(
            summary = "Добавить привилегии к роли",
            description = "Добавляет указанные привилегии к роли"
    )
    @PostMapping("/roles/{roleId}/privileges")
    public ResponseEntity<Role> addPrivilegesToRole(
            @PathVariable Long roleId,
            @RequestBody List<Long> privilegeIds) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Роль не найдена"));

        List<Privilege> privileges = privilegeRepository.findAllById(privilegeIds);

        if (role.getPrivileges() != null) {
            role.getPrivileges().addAll(privileges);
        } else {
            role.setPrivileges(privileges);
        }

        return ResponseEntity.ok(roleRepository.save(role));
    }
}