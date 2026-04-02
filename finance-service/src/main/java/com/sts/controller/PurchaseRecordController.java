package com.sts.controller;

import com.sts.dto.PurchaseRecordResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.PurchaseRecordService;
import com.sts.utils.constant.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AppConstants.FINANCE_BASE_PATH)
@RequiredArgsConstructor
@Slf4j
public class PurchaseRecordController {

    private final PurchaseRecordService purchaseRecordService;

    @GetMapping("/purchases")
    public ResponseEntity<PagedResponse<List<PurchaseRecordResponse>>> getAllPurchases(PageRequestDto pageRequestDto) {
        log.info("Fetch purchase request - page: {}, size: {}", pageRequestDto.getPage(), pageRequestDto.getSize());
        return AppResponse.success(purchaseRecordService.getAllPurchases(pageRequestDto.buildPageable()), AppConstants.SUCCESS_MESSAGES.PURCHASE_FETCHED);
    }


}

