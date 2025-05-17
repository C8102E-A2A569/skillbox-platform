package com.skillbox.security.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface CommonSecurityService {

    Optional<? extends UserDetails> findByUsername(String username);
}
