package com.skillbox.repository.mongo;

import com.skillbox.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserMongoRepository extends MongoRepository<User, String> {
    Optional<User> findById(String id);

    Optional<User> findByName(String name);
}