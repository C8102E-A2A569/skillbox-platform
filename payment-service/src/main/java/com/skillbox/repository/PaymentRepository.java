package com.skillbox.repository;

import com.skillbox.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    Optional<Payment> findById(String id);
    Optional<Payment> findByPaymentLink(String paymentLink);
    boolean existsByUserIdAndCourseIdAndPaymentLink(String userId, String courseId, String paymentLink);
}