package com.sts.dto.request;

import jakarta.validation.constraints.NotNull;

public record TenantRequest(
        @NotNull String companyName,
        @NotNull String email,
        @NotNull String username,
        @NotNull String password,
        @NotNull String adminPhone) {
}
