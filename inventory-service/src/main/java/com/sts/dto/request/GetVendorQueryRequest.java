package com.sts.dto.request;

public record GetVendorQueryRequest(
        String name,
        String contactNumber,
        String panNumber
) {
}
