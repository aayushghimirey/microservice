package com.sts.dto.request;

public record CreateStaffRequest(
        String name,
        String address,
        String role,
        String contactNumber,
        String[] permissions
) {
}
