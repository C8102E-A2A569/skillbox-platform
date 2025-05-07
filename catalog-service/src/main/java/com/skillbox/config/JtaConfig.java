package com.skillbox.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
public class JtaConfig {

    @Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
    public UserTransactionManager atomikosTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }

    @Bean
    public UserTransaction userTransaction() throws Throwable {
        return new UserTransactionImp();
    }

    @Bean
    public jakarta.transaction.TransactionManager jakartaTransactionManager(UserTransactionManager atomikosTransactionManager) {
        return atomikosTransactionManager;
    }

    @Bean(name = "transactionManager")
    @DependsOn("atomikosTransactionManager")
    public PlatformTransactionManager transactionManager(UserTransaction userTransaction,
                                                         UserTransactionManager atomikosTransactionManager) {
        return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
    }

}

