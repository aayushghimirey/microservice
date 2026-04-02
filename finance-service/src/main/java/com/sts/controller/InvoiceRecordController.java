package com.sts.controller;

import com.sts.dto.InvoiceRecordResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.InvoiceRecordService;
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
public class InvoiceRecordController {

    private final InvoiceRecordService invoiceRecordService;

    @GetMapping("/invoices")
    public ResponseEntity<PagedResponse<List<InvoiceRecordResponse>>> getAllInvoices(PageRequestDto pageRequestDto) {
        log.info("Fetch invoice request - page: {}, size: {}", pageRequestDto.getPage(), pageRequestDto.getSize());
        return AppResponse.success(invoiceRecordService.getAllInvoiceRecords(pageRequestDto.buildPageable()), AppConstants.SUCCESS_MESSAGES.INVOICE_FETCHED);
    }

}
