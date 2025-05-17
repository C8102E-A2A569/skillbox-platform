package com.skillbox.jms.producer;

import com.skillbox.jms.entity.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProducer {

    private final JmsTemplate jmsTemplate;

    public void sendEmail() {
        jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));
    }
}
