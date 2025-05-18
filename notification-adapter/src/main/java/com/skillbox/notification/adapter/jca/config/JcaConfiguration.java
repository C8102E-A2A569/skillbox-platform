package com.skillbox.notification.adapter.jca.config;

import com.skillbox.notification.adapter.jca.connector.NotificationConnection;
import com.skillbox.notification.adapter.jca.connector.NotificationConnectionFactory;
import com.skillbox.notification.adapter.jca.connector.impl.DummyConnectionManager;
import com.skillbox.notification.adapter.jca.connector.impl.NotificationConnectionFactoryImpl;
import com.skillbox.notification.adapter.jca.connector.impl.NotificationConnectionImpl;
import com.skillbox.notification.adapter.jca.connector.impl.NotificationManagedConnectionFactory;
import com.skillbox.notification.adapter.rabbitmq.service.NotificationProducer;
import jakarta.resource.spi.ConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JcaConfiguration {
    @Bean
    public NotificationConnectionFactory notificationConnectionFactory(ConnectionManager connectionManager, NotificationManagedConnectionFactory notificationManagedConnectionFactory, NotificationConnection notificationConnectionImpl) {
        return new NotificationConnectionFactoryImpl(notificationManagedConnectionFactory, connectionManager, notificationConnectionImpl);
    }

    @Bean
    public NotificationConnection notificationConnection(NotificationProducer producer) {
        return new NotificationConnectionImpl(producer);
    }

    @Bean
    public ConnectionManager notificationConnectionManager() {
        return new DummyConnectionManager();
    }

    @Bean
    public NotificationManagedConnectionFactory notificationManagedConnectionFactory() {
        return new NotificationManagedConnectionFactory();
    }


}
