package com.skillbox.notification.adapter.jca.connector.impl;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionFactory;
import jakarta.resource.spi.ConnectionManager;

import javax.security.auth.Subject;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 *
 */
public class NotificationManagedConnectionFactory implements ManagedConnectionFactory {

    private final UUID serialNum;
    private PrintWriter logWriter;

    public NotificationManagedConnectionFactory() {
        serialNum = UUID.randomUUID();
    }

    @Override
    public Object createConnectionFactory(ConnectionManager cxManager) throws ResourceException {
        return new NotificationConnectionFactoryImpl(this, cxManager, null);
    }

    @Override
    public Object createConnectionFactory() throws ResourceException {
        throw new ResourceException("ConnectionManager is required");
    }

    @Override
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        return new NotificationManagedConnection();
    }

    @Override
    public ManagedConnection matchManagedConnections(Set set, Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) {
        this.logWriter = out;
    }

    @Override
    public PrintWriter getLogWriter() {
        return this.logWriter;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NotificationManagedConnectionFactory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNum, logWriter);
    }
}
