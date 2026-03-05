package com.sts.dto.request;

public record TableRequest(
        String name,
        String location,
        int capacity) {
}
