package com.skillbox.notification.adapter.jca.connector.impl;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;

public class NotificationManagedConnection implements ManagedConnection {
    private PrintWriter logWriter;
    private NotificationConnectionImpl connection;

    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        return new NotificationConnectionImpl(null);
    }

    /*
        Unused (contract) methods:
     */
    @Override
    public void destroy() throws ResourceException {
    }

    @Override
    public void cleanup() throws ResourceException {
    }

    @Override
    public void associateConnection(Object o) throws ResourceException {
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener connectionEventListener) {
    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener connectionEventListener) {
    }

    @Override
    public XAResource getXAResource() throws ResourceException {
        return null;
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        return null;
    }

    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws ResourceException {
        this.logWriter = printWriter;
    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        return logWriter;
    }
}
