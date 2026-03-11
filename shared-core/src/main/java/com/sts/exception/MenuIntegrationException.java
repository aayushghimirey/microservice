package com.sts.exception;

/**
 * Custom exception for Menu Service integration failures.
 */
public class MenuIntegrationException extends RuntimeException {
    public MenuIntegrationException(String message) {
        super(message);
    }
}
