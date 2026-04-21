package com.sts.controller;

import com.sts.dto.request.TenantRequest;
import com.sts.model.Tenant;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<Tenant>>> getAllTenants() {
        return AppResponse.success(tenantService.getAllTenants(), "Tenant fetched successfully");
    }


}
