package com.sts.service;

import com.sts.dto.InvoiceRecordResponse;
import com.sts.mapper.InvoiceRecordMapper;
import com.sts.repository.InvoiceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceRecordService {

    private final InvoiceRecordRepository invoiceRecordRepository;
    private final InvoiceRecordMapper invoiceRecordMapper;

    public Page<InvoiceRecordResponse> getAllInvoiceRecords(Pageable pageable) {
        return invoiceRecordRepository.findAll(pageable).map(invoiceRecordMapper::toResponse);
    }

}
