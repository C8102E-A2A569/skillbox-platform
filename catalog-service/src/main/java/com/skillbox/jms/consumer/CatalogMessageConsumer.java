package com.skillbox.jms.consumer;

import com.skillbox.common.jms.entity.Email;
import com.skillbox.common.jms.entity.PaymentOperationMessage;
import com.skillbox.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogMessageConsumer {
    private final CatalogService catalogService;

    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(Email email) {
        System.out.println("Received <" + email + ">");
    }

    @JmsListener(destination = "payments", containerFactory = "myFactory")
    public void receivePayment(PaymentOperationMessage payment) {
        System.out.println("Received payment! [" + payment + "]");
        catalogService.addUserToCourse(payment.getUserId(), payment.getCourseId());
    }
}
