package com.sts.controller;

import com.sts.dto.request.CreateVendorCommand;
import com.sts.dto.request.GetVendorQueryRequest;
import com.sts.dto.request.UpdateVendorCommand;
import com.sts.dto.response.VendorResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.VendorService;
import com.sts.utils.constant.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(AppConstants.VENDOR_BASE_PATH)
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public ResponseEntity<ApiResponse<VendorResponse>> createVendor(
            @Valid @RequestBody CreateVendorCommand command) {

        VendorResponse vendor = vendorService.createVendor(command);

        return AppResponse.success(vendor, AppConstants.Response.VENDOR_CREATED);
    }

    @PatchMapping("/{vendorId}")
    public ResponseEntity<ApiResponse<VendorResponse>> updateVendor(
            @PathVariable UUID vendorId,
            @Valid @RequestBody UpdateVendorCommand command) {
        VendorResponse vendor = vendorService.updateVendor(vendorId, command);

        return AppResponse.success(vendor, AppConstants.Response.VENDOR_UPDATED);

    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<VendorResponse>>> getAllVendors(
            @ModelAttribute GetVendorQueryRequest queryRequest,
            PageRequestDto pageRequestDto) {
        var vendors = vendorService.getAllVendors(queryRequest, pageRequestDto.buildPageable());
        return AppResponse.success(vendors, AppConstants.Response.FETCHED_VENDORS);

    }

}
