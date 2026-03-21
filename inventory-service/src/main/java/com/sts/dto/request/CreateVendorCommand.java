package com.sts.dto.request;

import com.sts.utils.constant.AppConstants;
import jakarta.validation.constraints.NotBlank;

public record CreateVendorCommand(
        @NotBlank(message = AppConstants.VALIDATION_MESSAGES.VENDOR_NAME_REQUIRED)
        String name,
        @NotBlank(message = AppConstants.VALIDATION_MESSAGES.ADDRESS_REQUIRED)
        String address,
        @NotBlank(message = AppConstants.VALIDATION_MESSAGES.CONTACT_NUMBER_REQUIRED)
        String contactNumber,
        @NotBlank(message = AppConstants.VALIDATION_MESSAGES.PAN_NUMBER_REQUIRED)
        String panNumber) {
}
