package com.sts.controller;

import com.sts.domain.BusinessDetailResponse;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.service.BusinessDetailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth/public/business-details")
@RequiredArgsConstructor
public class PublicBusinessDetail {

    private static final Logger log = LoggerFactory.getLogger(PublicBusinessDetail.class);
    private final BusinessDetailService businessDetailService;


    @GetMapping("/{tenantId}")
    public ResponseEntity<ApiResponse<BusinessDetailResponse>> getBusinessDetail(@PathVariable("tenantId") UUID tenantId) {
        log.info("Received request to fetch business details for tenantId: {}", tenantId);
        return AppResponse.success(businessDetailService.getBusinessDetailPublic(tenantId), "Business details retrieved successfully");
    }

}
