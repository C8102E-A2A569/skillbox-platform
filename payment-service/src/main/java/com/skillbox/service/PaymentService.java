package com.skillbox.service;

import com.skillbox.client.UserClient;
import com.skillbox.client.dto.UserDto;
import com.skillbox.common.jms.entity.PaymentOperationMessage;
import com.skillbox.dto.PaymentRequest;
import com.skillbox.exception.ErrorResponse;
import com.skillbox.jms.producer.PaymentProducer;
import com.skillbox.model.Bank;
import com.skillbox.model.Payment;
import com.skillbox.model.PaymentStatus;
import com.skillbox.repository.BankRepository;
import com.skillbox.repository.PaymentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final double COURSE_PRICE = 100;

    private final PaymentRepository paymentRepository;
    private final BankRepository bankRepository;
    private final PlatformTransactionManager transactionManager;

    private final UserClient userClient;
    private final PaymentProducer producer;

    @PostConstruct
    public void connectivityCheck() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        scheduledExecutorService.schedule(() -> {
            try {
                UserDto userById = userClient.getUserByName("admin");
                System.out.println("\nTest reach Catalog-service");
                System.out.println(userById);
                System.out.println("\n");
            } catch (Exception ignored) {
                System.out.println("\nWARNING: Catalog-service unreachable!\n\n");
            }
        }, 10, TimeUnit.SECONDS);

    }

    public String createPayment(PaymentRequest request) {

        Optional<Payment> paymentOptional = paymentRepository.findByUserIdAndCourseId(request.getUserId(), request.getCourseId());
        if (paymentOptional.isPresent()) {
            Payment paid = paymentOptional.get();
            throw ErrorResponse.paymentLinkAlreadyExists(paid.getUserId(), paid.getCourseId(), paid.getPaymentLink(), paid.getExpiresAt().toString());
        }

        String paymentLink = "https://sberbank/pay/" + UUID.randomUUID();

        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setCourseId(request.getCourseId());
        payment.setTariff(request.getTariff());
        payment.setName(request.getName());
        payment.setEmail(request.getEmail());
        payment.setPaymentLink(paymentLink);
        payment.setStatus("PENDING");
        payment.setExpiresAt(LocalDateTime.now().plusDays(14));

        paymentRepository.save(payment);

        return paymentLink;
    }

    /**
     * substracts COURSE_PRICE (100 coins) form user's balance (or creates with given amount and substracts)
     * @param userId user paying for course
     * @param paymentLink generated payment link
     * @param amount amount of money user wnats to have on its account MUST BE > COURSE_PRICE (100 coins)
     * @return payment lonk
     */
    public String processPayment(String userId, String paymentLink, double amount) {
        // Define transaction
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            Bank bank = bankRepository.findByUserId(userId)
                    .orElseGet(() -> bankRepository.save(new Bank(UUID.randomUUID().toString(), userId, amount)));

            if (amount <= 0) {
                throw ErrorResponse.negativeAmount();
            }

            Payment payment = paymentRepository.findByPaymentLink(paymentLink)
                    .orElseThrow(() -> ErrorResponse.paymentLinkNotFound(paymentLink));

            if (payment.getExpiresAt().isBefore(LocalDateTime.now())) {
                payment.setStatus(PaymentStatus.FAILED.name());
                paymentRepository.save(payment);
                throw ErrorResponse.paymentLinkExpired();
            }

            if (PaymentStatus.SUCCESS.name().equals(payment.getStatus())) {
                throw ErrorResponse.paymentAlreadyProcessed(paymentLink);
            }

            if (bank.getBalance() >= COURSE_PRICE) {
                bank.setBalance(bank.getBalance() - COURSE_PRICE);
                bankRepository.save(bank);

                payment.setStatus(PaymentStatus.SUCCESS.name());
                paymentRepository.save(payment);


//                restTemplate.put(enrollUrl, null); todo artur вот тут можно jms, отправка
                // send by rabbitMQ successfull payment request
                producer.sendPaymentInfo(
                        new PaymentOperationMessage(payment.getUserId(), payment.getCourseId())
                );


                // Commit transaction
                transactionManager.commit(status);
                return "Payment success чмок *з* текущий баланс пользователя: " + bank.getBalance();
            } else {
                // Commit transaction (no changes were made)
                transactionManager.commit(status);
                return "NOT ENOUGH money :( your balance = %f.2, course price = %f.2 coins".formatted(bank.getBalance(), COURSE_PRICE);
            }
        } catch (Exception e) {
            // Rollback transaction on error
            transactionManager.rollback(status);
            throw e;
        }
    }

}
