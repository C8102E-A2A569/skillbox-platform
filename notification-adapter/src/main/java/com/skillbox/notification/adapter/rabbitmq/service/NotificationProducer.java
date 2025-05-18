package com.skillbox.notification.adapter.rabbitmq.service;

import com.skillbox.notification.adapter.rabbitmq.config.NotificationAdapterProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    private final NotificationAdapterProperties properties;

    public NotificationProducer(RabbitTemplate rabbitTemplate, NotificationAdapterProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }


    public void send(Object message) {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(properties.getExchangeName(), properties.getRoutingKey(), message);
    }
}
