package com.sts.response;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

public final class AppResponse {

    // Normal response
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(
                new ApiResponse<>(message, "SUCCESS", data)
        );
    }

    // Paged response
    public static <T> ResponseEntity<PagedResponse<List<T>>> success(Page<T> page, String message) {
        return ResponseEntity.ok(
                new PagedResponse<>(
                        message,
                        "SUCCESS",
                        page.getContent(),
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.isLast()
                )
        );
    }

    // No Response
    public static ResponseEntity<ApiResponse<Void>> noContent() {
        return ResponseEntity.noContent().build();
    }
}