package com.skillbox.repository;

import com.skillbox.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    Optional<Payment> findById(String id);
    Optional<Payment> findByPaymentLink(String paymentLink);
    boolean existsByUserIdAndCourseIdAndPaymentLink(String userId, String courseId, String paymentLink);

    Optional<Payment> findByUserIdAndCourseIdAndStatus(String userId, String courseId, String status);

    List<Payment> findAllByExpiresAtAfterAndStatus(LocalDateTime currentTime, String status);
    List<Payment> findAllByExpiresAtBeforeAndStatus(LocalDateTime currentTime, String status);
}