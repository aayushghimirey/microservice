package com.sts.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String username,
        String role) {

}

