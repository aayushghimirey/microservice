package com.sts.feign;

import com.sts.domain.BusinessDetailResponse;
import com.sts.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "business-detail-service", url = "${app.clients.business-service.url}", path = "/auth/public/business-details")
public interface BusinessDetailClient {

    @GetMapping("/{tenantId}")
    ResponseEntity<ApiResponse<BusinessDetailResponse>> getBusinessDetail(@PathVariable("tenantId") UUID tenantId);

}
