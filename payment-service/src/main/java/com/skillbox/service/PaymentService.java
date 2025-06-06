package com.skillbox.service;

import com.skillbox.dto.PaymentRequest;
import com.skillbox.exception.ErrorResponse;
import com.skillbox.model.Bank;
import com.skillbox.model.Payment;
import com.skillbox.repository.BankRepository;
import com.skillbox.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BankRepository bankRepository;
    private final RestTemplate restTemplate;
    private final PlatformTransactionManager transactionManager;

    private String catalogServiceUrl =  "http://catalog-service:8080";

    @PreAuthorize("hasAuthority('CREATE_PAYMENT')")
    public String createPayment(PaymentRequest request) {
        String paymentLink = "https://bank.example.com/pay/" + UUID.randomUUID();

        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setCourseId(request.getCourseId());
        payment.setTariff(request.getTariff());
        payment.setName(request.getName());
        payment.setEmail(request.getEmail());
        payment.setPaymentLink(paymentLink);
        payment.setStatus("PENDING");
        payment.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        paymentRepository.save(payment);

        return paymentLink;
    }


    @PreAuthorize("hasAuthority('PROCESS_PAYMENT')")
    public String processPayment(String userId, String paymentLink, double amount) {
        // Define transaction
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            Bank bank = bankRepository.findByUserId(userId)
                    .orElseThrow(() -> ErrorResponse.bankNotFound(userId));

            if (amount != (int) amount) {
                throw ErrorResponse.invalidAmountType();
            }

            if (amount <= 0) {
                throw ErrorResponse.negativeAmount();
            }

            Payment payment = paymentRepository.findByPaymentLink(paymentLink)
                    .orElseThrow(() -> ErrorResponse.paymentLinkNotFound(paymentLink));

            if (payment.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw ErrorResponse.paymentLinkExpired();
            }

            if ("SUCCESS".equals(payment.getStatus())) {
                throw ErrorResponse.paymentAlreadyProcessed(paymentLink);
            }

            if (bank.getBalance() >= amount) {
                bank.setBalance(bank.getBalance() - amount);
                bankRepository.save(bank);

                payment.setStatus("SUCCESS");
                paymentRepository.save(payment);

                String enrollUrl = catalogServiceUrl + "/users/" + userId + "/enroll/" + payment.getCourseId();
                restTemplate.put(enrollUrl, null);

                // Commit transaction
                transactionManager.commit(status);
                return "SUCCESS";
            } else {
                // Commit transaction (no changes were made)
                transactionManager.commit(status);
                return "INSUFFICIENT_FUNDS";
            }
        } catch (Exception e) {
            // Rollback transaction on error
            transactionManager.rollback(status);
            throw e;
        }
    }

}
