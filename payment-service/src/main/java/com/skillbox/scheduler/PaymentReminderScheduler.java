package com.skillbox.scheduler;

import com.atomikos.datasource.ResourceException;
import com.skillbox.common.jca.entity.PaymentNotification;
import com.skillbox.common.jms.entity.Email;
import com.skillbox.model.Payment;
import com.skillbox.model.PaymentStatus;
import com.skillbox.notification.adapter.jca.connector.NotificationConnection;
import com.skillbox.notification.adapter.jca.connector.NotificationConnectionFactory;
import com.skillbox.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentReminderScheduler {

    // jms
    private final JmsTemplate jmsTemplate;
    // jca
    private final NotificationConnectionFactory notificationConnectionFactory;
    private final PaymentRepository paymentRepository;


    /**
     * TEST Scheduler initial delay 15s, and then every 5s
     */
//    @Scheduled(initialDelay = 15_000, fixedDelay = 5_000)
    @Deprecated
    public void sendUsingJms() {
        jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));
    }

    /**
     * Scheduled every day at 00:00
     */
//    @Scheduled(cron = "0 0 * * *")
    @Scheduled(initialDelay = 15_000, fixedDelay = 5_000)
    public void sendUnpaidNotifications(){
        log.info("[TECH] sending JCA req");
        NotificationConnection connection = notificationConnectionFactory.getConnection();
        List<Payment> freshPayments = paymentRepository.findAllByExpiresAtAfter(LocalDateTime.now());
        if (CollectionUtils.isEmpty(freshPayments)) {
            return;
        }
        freshPayments.forEach(
                payment -> connection.sendNotification(
                        new PaymentNotification(payment.getUserId(), payment.getId(), payment.getEmail())
                )
        );
    }

    /**
     * Scheduled every day at 00:00
     */
//    @Scheduled(cron = "0 0 * * *")
    @Scheduled(initialDelay = 15_000, fixedDelay = 5_000)
    public void deleteOutdatedPayments(){
        log.info("[TECH] deleting outdated payments");
        List<Payment> outdatedPayments = paymentRepository.findAllByExpiresAtBeforeAndStatus(LocalDateTime.now(), PaymentStatus.PENDING.name());
        if (CollectionUtils.isEmpty(outdatedPayments)) {
            return;
        }
        outdatedPayments.forEach(p -> {
            log.info("[TECH] payment with id " + p.getId());
            p.setStatus(PaymentStatus.FAILED.name());
            paymentRepository.save(p);
        });


    }
}
