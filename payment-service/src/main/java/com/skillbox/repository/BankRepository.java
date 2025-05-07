package com.skillbox.repository;

import com.skillbox.model.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends MongoRepository<Bank, String> {
    Optional<Bank> findByUserId(String userId);
    List<Bank> findAll();
}
