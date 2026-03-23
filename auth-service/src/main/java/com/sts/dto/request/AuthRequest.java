package com.sts.dto.request;

public record AuthRequest(
        String username,
        String password
) {
}
