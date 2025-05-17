package com.skillbox.repository.sql;

import com.skillbox.security.entity.Role;
import com.skillbox.security.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);

    Optional<UserAccount> findByMongoUserId(String mongoUserId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    int countByRolesContaining(Role role);
}
