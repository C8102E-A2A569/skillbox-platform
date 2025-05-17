package com.skillbox.scheduler;

import com.skillbox.jms.entity.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerJMSExample {

    private final JmsTemplate jmsTemplate;

    /**
     * Scheduled initial delay 15s, and then every 5s
     */
    @Scheduled(initialDelay = 15_000, fixedDelay = 5_000)
    public void sendUsingJms() {
        jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));
    }
}
