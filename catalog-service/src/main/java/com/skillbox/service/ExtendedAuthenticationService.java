package com.skillbox.service;

import com.skillbox.dto.auth.AuthenticationResponse;
import com.skillbox.dto.auth.ExtendedRegisterRequest;
import com.skillbox.entity.Role;
import com.skillbox.entity.UserAccount;
import com.skillbox.model.User;
import com.skillbox.repository.mongo.UserRepository;
import com.skillbox.repository.sql.RoleRepository;
import com.skillbox.repository.sql.UserAccountRepository;
import com.skillbox.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExtendedAuthenticationService {

    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final UserRepository mongoUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthenticationResponse registerWithRoles(ExtendedRegisterRequest request) {
        // Create MongoDB user
        User mongoUser = new User();
        mongoUser.setName(request.getUsername());
        mongoUser.setEmail(request.getEmail());
        mongoUser.setEnrolledCourses(Collections.emptyList());
        mongoUser = mongoUserRepository.save(mongoUser);

        // Get roles
        List<Role> roles = new ArrayList<>();
        if (request.getRoleNames() != null && !request.getRoleNames().isEmpty()) {
            roles = request.getRoleNames().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Роль не найдена: " + roleName)))
                    .collect(Collectors.toList());
        } else {
            // Default role - ROLE_USER
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Роль ROLE_USER не найдена"));
            roles.add(userRole);
        }

        // Create PostgreSQL user account
        UserAccount userAccount = UserAccount.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .mongoUserId(mongoUser.getId())
                .roles(roles)
                .build();

        userAccountRepository.save(userAccount);

        String jwtToken = jwtService.generateToken(userAccount);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}