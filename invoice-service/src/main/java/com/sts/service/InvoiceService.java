package com.sts.service;

import com.sts.dto.InvoiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {

    InvoiceResponse proceedInvoice(UUID invoiceId);

    List<InvoiceResponse> getAllPendingInvoices();

    Page<InvoiceResponse> getAllInvoices(Pageable pageable);

}
