package com.sts.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record VendorResponse(
        UUID id,
        String name,
        String contactNumber,
        String panNumber,
        String address
) {
}
