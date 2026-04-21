package com.sts.service;

import com.sts.dto.CreateInvoiceCommand;
import com.sts.dto.InvoiceResponse;
import com.sts.dto.InvoiceSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {

    InvoiceResponse proceedInvoice(UUID invoiceId, CreateInvoiceCommand command);

    List<InvoiceResponse> getAllPendingInvoices();

    Page<InvoiceResponse> getAllInvoices(InvoiceSearchRequest request, Pageable pageable);

    String printInvoice(UUID invoiceId);

}
