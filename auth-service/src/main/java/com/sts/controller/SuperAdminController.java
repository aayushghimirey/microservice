package com.sts.controller;

import com.sts.dto.request.TenantRequest;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/super")
@RequiredArgsConstructor
public class SuperAdminController {

    private final TenantService tenantService;

    @PostMapping("/tenant")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> createTenant(@RequestBody TenantRequest request) {
        tenantService.registerTenant(request);
        return AppResponse.success("", "Tenant created successfully");
    }

}
