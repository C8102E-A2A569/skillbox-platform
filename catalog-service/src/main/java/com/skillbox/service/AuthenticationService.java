package com.skillbox.service;

import com.skillbox.dto.auth.AuthenticationRequest;
import com.skillbox.dto.auth.AuthenticationResponse;
import com.skillbox.dto.auth.RegisterRequest;
import com.skillbox.entity.Role;
import com.skillbox.entity.UserAccount;
import com.skillbox.exception.ErrorResponse;
import com.skillbox.model.User;
import com.skillbox.repository.mongo.UserRepository;
import com.skillbox.repository.sql.RoleRepository;
import com.skillbox.repository.sql.UserAccountRepository;
import com.skillbox.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final UserRepository mongoUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        // Create MongoDB user
        User mongoUser = new User();
        mongoUser.setName(request.getUsername());
        mongoUser.setEmail(request.getEmail());
        mongoUser.setEnrolledCourses(Collections.emptyList());
        mongoUser = mongoUserRepository.save(mongoUser);

        // Create PostgreSQL user account
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        UserAccount userAccount = UserAccount.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .mongoUserId(mongoUser.getId())
                .roles(Collections.singletonList(userRole))
                .build();

        userAccountRepository.save(userAccount);

        String jwtToken = jwtService.generateToken(userAccount);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // код ниже проверяет ТОКЕН на валидность, а наш пользователь еще его не должен иметь, он только что зарегистрировался,
        // и только после логина будем проверять на валидность токен, а пока не надо
        /*
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        */

        UserAccount userAccount = userAccountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> ErrorResponse.userNotFoundByUsername(request.getUsername()));

        if (!passwordEncoder.matches(request.getPassword(), userAccount.getPassword())) {
            throw ErrorResponse.userPasswordMismatch(request.getUsername());
        }

        String jwtToken = jwtService.generateToken(userAccount);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
