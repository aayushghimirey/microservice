package com.sts.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.sts.response.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatePurchase.class)
    public ResponseEntity<ErrorResponse> handleException(DuplicatePurchase e, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), request.getRequestURI(), List.of());
    }

    @ExceptionHandler(VariantNotFound.class)
    public ResponseEntity<ErrorResponse> handleException(VariantNotFound e, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), request.getRequestURI(), List.of());
    }

    @ExceptionHandler(UnitNotFound.class)
    public ResponseEntity<ErrorResponse> handleException(UnitNotFound e, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), request.getRequestURI(), List.of());
    }

    @ExceptionHandler(StockNotFound.class)
    public ResponseEntity<ErrorResponse> handleException(StockNotFound e, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), request.getRequestURI(), List.of());
    }

    @ExceptionHandler(DuplicateStock.class)
    public ResponseEntity<ErrorResponse> handleException(DuplicateStock e, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), request.getRequestURI(), List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex, HttpServletRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred",
                request.getRequestURI(), List.of());
    }


    private ResponseEntity<ErrorResponse> build(
            HttpStatus status, String message, String path, List<ErrorResponse.FieldError> errors) {
        return ResponseEntity.status(status).body(new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                errors));
    }
}
