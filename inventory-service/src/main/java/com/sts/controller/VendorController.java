package com.sts.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.dto.request.CreateVendorCommand;
import com.sts.dto.response.VendorResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.VendorService;
import com.sts.utils.contant.AppConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(AppConstants.VENDOR_BASE_PATH)
@AllArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public ResponseEntity<ApiResponse<VendorResponse>> createVendor(
            @Valid @RequestBody CreateVendorCommand command) {
        log.info(AppConstants.LOG_MESSAGES.CREATING_VENDOR, command);
        return AppResponse.success(vendorService.createVendor(command),
                AppConstants.SUCCESS_MESSAGES.VENDOR_CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<VendorResponse>>> getAllVendors(
            PageRequestDto pageRequestDto) {
        return AppResponse.success(vendorService.getAllVendors(pageRequestDto.buildPageable()),
                AppConstants.SUCCESS_MESSAGES.VENDORS_FETCHED);
    }

}
