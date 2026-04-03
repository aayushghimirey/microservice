package com.sts.controller;

import com.sts.dto.InvoiceRecordResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.FinanceService;
import com.sts.service.interfaces.InvoiceRecordService;
import com.sts.utils.constant.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AppConstants.FINANCE_BASE_PATH + "/invoices")
@RequiredArgsConstructor
public class InvoiceRecordController extends AbstractFinanceController<InvoiceRecordResponse> {

    private final InvoiceRecordService invoiceRecordService;


    @Override
    protected FinanceService<InvoiceRecordResponse> getService() {
        return invoiceRecordService;
    }

    @Override
    protected String getSuccessMessage() {
        return AppConstants.SUCCESS_MESSAGES.INVOICE_FETCHED;
    }


    @Override
    @GetMapping
    public ResponseEntity<PagedResponse<List<InvoiceRecordResponse>>> getAllRecords(PageRequestDto pageRequest) {
        return super.getAllRecords(pageRequest);
    }

}
