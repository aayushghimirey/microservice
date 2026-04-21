package com.sts.controller;

import com.sts.domain.BusinessDetailResponse;
import com.sts.dto.request.BusinessDetailRequest;
import com.sts.dto.request.BusinessDetailUpdate;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.service.BusinessDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/business-details")
public class BusinessDetailController {

    private final BusinessDetailService businessDetailService;

    @PostMapping
    public ResponseEntity<ApiResponse<BusinessDetailResponse>> createBusinessDetail(@Valid @RequestBody BusinessDetailRequest request) {
        return AppResponse.success(businessDetailService.createBusinessDetail(request), "Business details created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<BusinessDetailResponse>> getBusinessDetail() {
        return AppResponse.success(businessDetailService.getBusinessDetail(), "Business details retrieved successfully");
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<BusinessDetailResponse>> updateBusinessDetail(
            @Valid @RequestBody BusinessDetailUpdate request) {
        return AppResponse.success(businessDetailService.updateBusinessDetail(  request), "Business details updated successfully");
    }

}
