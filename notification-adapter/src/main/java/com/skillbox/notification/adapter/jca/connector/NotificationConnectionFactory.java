package com.skillbox.notification.adapter.jca.connector;

/**
 * Фабрика, из которой приложение получает NotificationConnection.
 * Она хранит ссылку на ManagedConnectionFactory и ConnectionManager.
 */
public interface NotificationConnectionFactory {

    NotificationConnection getConnection();
}
