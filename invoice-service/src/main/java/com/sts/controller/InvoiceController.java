package com.sts.controller;

import com.sts.dto.InvoiceResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.InvoiceService;
import com.sts.utils.constant.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AppConstants.INVOICE_BASE_PATH)
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getAllPending() {
        return AppResponse.success(invoiceService.getAllPendingInvoices(), AppConstants.SUCCESS_MESSAGES.INVOICES_FETCHED);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<InvoiceResponse>>> getAllInvoices(PageRequestDto pageRequestDto) {
        return AppResponse.success(invoiceService.getAllInvoices(pageRequestDto.buildPageable()), AppConstants.SUCCESS_MESSAGES.INVOICES_FETCHED);
    }


}
