package com.sts.exception;

public class OutboxPublishException extends RuntimeException{
    public OutboxPublishException(String message) {
        super(message);
    }
}
