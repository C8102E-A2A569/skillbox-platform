package com.skillbox.security;

import com.skillbox.repository.UserAccountsRepository;
import com.skillbox.security.entity.UserAccount;
import com.skillbox.security.service.CommonSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommonSecurityServiceImpl implements CommonSecurityService {

    private final UserAccountsRepository userAccountsRepository;

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        return userAccountsRepository.findByUsername(username);
    }
}

