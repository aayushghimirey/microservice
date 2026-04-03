package com.sts.controller;

import com.sts.dto.InvoiceRecordResponse;
import com.sts.dto.PurchaseRecordResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.PagedResponse;
import com.sts.service.FinanceService;
import com.sts.service.interfaces.PurchaseRecordService;
import com.sts.utils.constant.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AppConstants.FINANCE_BASE_PATH + "/purchases")
@RequiredArgsConstructor
public class PurchaseRecordController extends AbstractFinanceController<PurchaseRecordResponse> {

    private final PurchaseRecordService purchaseRecordService;


    @Override
    protected FinanceService<PurchaseRecordResponse> getService() {
        return purchaseRecordService;
    }

    @Override
    protected String getSuccessMessage() {
        return AppConstants.SUCCESS_MESSAGES.PURCHASE_FETCHED;
    }

    @Override
    @GetMapping
    public ResponseEntity<PagedResponse<List<PurchaseRecordResponse>>> getAllRecords(PageRequestDto pageRequest) {
        return super.getAllRecords(pageRequest);
    }
}

