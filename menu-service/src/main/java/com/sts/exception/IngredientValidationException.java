package com.sts.exception;

/**
 * Thrown when an ingredient fails validation against the Inventory Service.
 */
public class IngredientValidationException extends RuntimeException {

    public IngredientValidationException(String message) {
        super(message);
    }
}
