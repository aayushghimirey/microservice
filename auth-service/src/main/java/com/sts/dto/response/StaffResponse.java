package com.sts.dto.response;

import lombok.Builder;

@Builder
public record StaffResponse(
        String name,
        String address,
        String role,
        String[] permissions,
        String contactNumber
) {
}
