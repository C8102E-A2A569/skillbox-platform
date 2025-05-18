package com.skillbox.notification.adapter.rabbitmq.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "notification.adapter")
public class NotificationAdapterProperties {

    private RabbitMQNotificationProperties rabbitmq = new RabbitMQNotificationProperties();
    private String exchangeName = "notification-exchange";
    private String routingKey = "notification.key";

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RabbitMQNotificationProperties {
        private String username = "rabbit";
        private String password = "rabbit";
        private String virtualHost = "/";
        private String host = "localhost";
        private Integer port = 5672;
    }
}
