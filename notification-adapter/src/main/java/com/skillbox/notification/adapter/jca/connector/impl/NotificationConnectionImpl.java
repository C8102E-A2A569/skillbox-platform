package com.skillbox.notification.adapter.jca.connector.impl;

import com.skillbox.notification.adapter.jca.connector.NotificationConnection;
import com.skillbox.notification.adapter.rabbitmq.service.NotificationProducer;
import lombok.AllArgsConstructor;

/**
 * Stateless connection. Thread-safe because all state is handled by RabbitTemplate.
 */
@AllArgsConstructor
public class NotificationConnectionImpl implements NotificationConnection {

    private final NotificationProducer producer;

    @Override
    public void sendNotification(Object message) {
        producer.send(message);
    }
}
