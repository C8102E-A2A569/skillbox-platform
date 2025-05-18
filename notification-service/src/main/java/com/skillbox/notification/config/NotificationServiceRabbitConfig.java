package com.skillbox.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class NotificationServiceRabbitConfig {

    @Value("${notification-service.rabbitmq.queue}")
    public String queueName;
    @Value("${notification-service.rabbitmq.exchange}")
    public String exchangeName;
    @Value("${notification-service.rabbitmq.key}")
    public String routingKey;

    @Bean
    public Queue notificationQueue() {
        return new Queue(queueName, true, false, false);
    }

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(routingKey);
    }

}
