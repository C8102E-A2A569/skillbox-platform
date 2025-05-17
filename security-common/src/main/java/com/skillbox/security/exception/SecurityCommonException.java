package com.skillbox.security.exception;

public class SecurityCommonException extends RuntimeException {

    public SecurityCommonException(String message) {
        super(message);
    }

    public SecurityCommonException(String message, Throwable cause) {
        super(message, cause);
    }
}
