package com.sts.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateVendorCommand(
        @NotBlank(message = "Vendor name is required") String name,
        String address,
        String contactNumber,
        String panNumber) {
}
