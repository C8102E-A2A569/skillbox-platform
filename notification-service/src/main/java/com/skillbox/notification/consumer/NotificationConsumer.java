package com.skillbox.notification.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.common.jca.entity.PaymentNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationConsumer {

    @RabbitListener(queues = "${notification-service.rabbitmq.queue}")
    public void handleMessage(Message message) {
        byte[] body = message.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
            PaymentNotification notification = mapper.readValue(body, PaymentNotification.class);
            log.info("💥 Received notification message: " + notification);
        } catch (Exception e) {
            log.error("Cannot deserialize message", e);
        }

    }
}
