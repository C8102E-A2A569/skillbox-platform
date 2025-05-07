package com.skillbox.repository.sql;

import com.skillbox.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);
    Optional<UserAccount> findByMongoUserId(String mongoUserId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
