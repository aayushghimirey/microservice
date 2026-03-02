package com.sts.command;

public record CreateTableCommand(
        String name,
        String location,
        int capacity
) {
}
