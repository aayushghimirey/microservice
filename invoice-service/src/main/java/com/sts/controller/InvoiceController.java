package com.sts.controller;

import java.util.List;
import java.util.UUID;

import com.sts.dto.CreateInvoiceCommand;
import com.sts.dto.InvoiceSearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sts.dto.InvoiceResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.InvoiceService;
import com.sts.utils.constant.AppConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(AppConstants.INVOICE_BASE_PATH)
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getAllPending() {
        return AppResponse.success(invoiceService.getAllPendingInvoices(),
                AppConstants.SUCCESS_MESSAGES.INVOICES_FETCHED);
    }

    @PostMapping("/{invoiceId}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> proceedInvoice(@PathVariable("invoiceId") UUID invoiceId,
                                                                       @RequestBody CreateInvoiceCommand command) {
        return AppResponse.success(invoiceService.proceedInvoice(invoiceId, command),
                AppConstants.SUCCESS_MESSAGES.INVOICE_CREATED);
    }

    @GetMapping("/{invoiceId}/print")
    public ResponseEntity<ApiResponse<String>> printInvoice(@PathVariable("invoiceId") UUID invoiceId) {
        return AppResponse.success(invoiceService.printInvoice(invoiceId), "Invoice printed successfully");
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<InvoiceResponse>>> getAllInvoices(
            InvoiceSearchRequest request,
            PageRequestDto pageRequestDto
    ) {
        return AppResponse.success(invoiceService.getAllInvoices(request, pageRequestDto.buildPageable()),
                AppConstants.SUCCESS_MESSAGES.INVOICES_FETCHED);
    }


}
