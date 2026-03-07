package com.sts.exception;

import com.sts.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMenuNotFound(
            MenuNotFoundException ex, HttpServletRequest request) {
        log.warn("Menu not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(IngredientValidationException.class)
    public ResponseEntity<ErrorResponse> handleIngredientValidation(
            IngredientValidationException ex, HttpServletRequest request) {
        log.warn("Ingredient validation failed: {}", ex.getMessage());
        return build(HttpStatus.valueOf(422), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Validation error on request to {}", request.getRequestURI());

        return build(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex, HttpServletRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred",
                request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> build(
            HttpStatus status, String message, String path) {
        return ResponseEntity.status(status).body(new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        ));
    }
}
