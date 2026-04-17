package com.sts.dto.request;

public record UpdateVendorCommand(
        String name,
        String address,
        String contactNumber,
        String panNumber
) {
}
