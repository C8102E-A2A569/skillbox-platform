package com.skillbox.notification.adapter.jca.connector.impl;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionManager;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionFactory;

public class DummyConnectionManager implements ConnectionManager {
    @Override
    public Object allocateConnection(ManagedConnectionFactory mcf, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        ManagedConnection mc = mcf.createManagedConnection(null, null);
        return mc.getConnection(null, null);
    }
}
