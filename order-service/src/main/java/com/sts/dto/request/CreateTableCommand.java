package com.sts.dto.request;

public record CreateTableCommand(
        String name,
        String location,
        int capacity) {
}
