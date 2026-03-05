package com.sts.exception;


public class TableNotOpenException extends RuntimeException {
    public TableNotOpenException(String message) {
        super(message);
    }
}
