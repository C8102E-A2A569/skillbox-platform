package com.skillbox.notification.adapter.jca.connector.impl;

import com.skillbox.notification.adapter.jca.connector.NotificationConnection;
import com.skillbox.notification.adapter.jca.connector.NotificationConnectionFactory;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionManager;
import lombok.RequiredArgsConstructor;

/**
 * Фабрика, из которой приложение получает NotificationConnection.
 * Она хранит ссылку на ManagedConnectionFactory и ConnectionManager.
 */
@RequiredArgsConstructor
public class NotificationConnectionFactoryImpl implements NotificationConnectionFactory {
    private final NotificationManagedConnectionFactory notificationManagedConnectionFactory;
    private final ConnectionManager cxManager;
    private final NotificationConnection notificationConnectionImpl;

    /**
     * Singleton because we don't have state (stateless bean, no need for new instances)
     */
    @Override
    public NotificationConnection getConnection(){
        return this.notificationConnectionImpl;
    }
}
