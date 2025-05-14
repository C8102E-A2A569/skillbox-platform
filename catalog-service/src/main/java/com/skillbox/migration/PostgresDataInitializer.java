package com.skillbox.migration;

import com.skillbox.entity.Privilege;
import com.skillbox.entity.Role;
import com.skillbox.entity.UserAccount;
import com.skillbox.repository.sql.PrivilegeRepository;
import com.skillbox.repository.sql.RoleRepository;
import com.skillbox.repository.sql.UserAccountRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostgresDataInitializer {

    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;
    private final PlatformTransactionManager transactionManager;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                // Create privileges if they don't exist
                Privilege readCatalog = createPrivilegeIfNotFound("READ_CATALOG");
                Privilege readUserInfo = createPrivilegeIfNotFound("READ_USER_INFO");
                Privilege enrollCourse = createPrivilegeIfNotFound("ENROLL_COURSE");
                Privilege manageCourses = createPrivilegeIfNotFound("MANAGE_COURSES");
                Privilege createPayment = createPrivilegeIfNotFound("CREATE_PAYMENT");
                Privilege processPayment = createPrivilegeIfNotFound("PROCESS_PAYMENT");
                Privilege adminPrivilege = createPrivilegeIfNotFound("ADMIN");

                // Create roles if they don't exist
                List<Privilege> studentPrivileges = Arrays.asList(
                        readCatalog, readUserInfo, enrollCourse, createPayment, processPayment
                );
                createRoleIfNotFound("ROLE_USER", studentPrivileges);

                List<Privilege> adminPrivileges = Arrays.asList(
                        readCatalog, readUserInfo, enrollCourse, manageCourses,
                        createPayment, processPayment, adminPrivilege
                );
                Role roleAdmin = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);

                if (userAccountRepository.count() == 0) {
                    UserAccount admin = UserAccount.builder()
                            .email("example@mail.ru")
                            .roles(List.of(roleAdmin))
                            .username("admin")
                            .password(passwordEncoder.encode("admin"))
                            .mongoUserId(String.valueOf(0))
                            .build();
                    userAccountRepository.save(admin);
                }

            }
        });
    }

    private Privilege createPrivilegeIfNotFound(String name) {
        return privilegeRepository.findByName(name)
                .orElseGet(() -> {
                    Privilege privilege = new Privilege();
                    privilege.setName(name);
                    return privilegeRepository.save(privilege);
                });
    }

    private Role createRoleIfNotFound(String name, List<Privilege> privileges) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    role.setPrivileges(privileges);
                    return roleRepository.save(role);
                });
    }
}
