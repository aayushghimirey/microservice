package com.sts.exception;

public class DuplicatePurchase extends RuntimeException {
    public DuplicatePurchase(String message) {

        super(message);
    }
}
