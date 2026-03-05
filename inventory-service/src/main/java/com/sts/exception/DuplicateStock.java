package com.sts.exception;

public class DuplicateStock extends RuntimeException{
    public DuplicateStock(String message) {
        super(message);
    }
}
