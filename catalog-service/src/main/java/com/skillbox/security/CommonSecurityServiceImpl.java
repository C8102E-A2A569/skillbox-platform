package com.skillbox.security;

import com.skillbox.repository.sql.UserAccountRepository;
import com.skillbox.common.security.entity.UserAccount;
import com.skillbox.security.service.CommonSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommonSecurityServiceImpl implements CommonSecurityService {

    private final UserAccountRepository repository;

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        return repository.findByUsername(username);
    }

}
