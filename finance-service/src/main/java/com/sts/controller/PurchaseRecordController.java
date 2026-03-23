package com.sts.controller;

import com.sts.pagination.PageRequestDto;
import com.sts.service.InvoiceRecordService;
import com.sts.service.PurchaseRecordService;
import com.sts.response.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finances")
@RequiredArgsConstructor
public class PurchaseRecordController {

    private final PurchaseRecordService purchaseRecordService;
    private final InvoiceRecordService invoiceRecordService;

    @GetMapping("/purchases")
    public ResponseEntity<?> getAllPurchases(PageRequestDto pageRequestDto) {
        return AppResponse.success(purchaseRecordService.getAllPurchases(pageRequestDto.buildPageable()), "SUccess");
    }

    @GetMapping("/invoices")
    public ResponseEntity<?> getAllInvoices(PageRequestDto pageRequestDto) {
        return AppResponse.success(invoiceRecordService.getAllInvoiceRecords(pageRequestDto.buildPageable()), "Success");
    }

}

