package com.sts.exception;

public class StockNotFound extends RuntimeException{
    public StockNotFound(String message) {
        super(message);
    }
}
