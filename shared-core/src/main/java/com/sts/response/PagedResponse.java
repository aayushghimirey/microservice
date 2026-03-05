package com.sts.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagedResponse<T> extends ApiResponse<T> {

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public PagedResponse(
            String message,
            String status,
            T data,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean last
    ) {
        super(message, status, data);
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }
}