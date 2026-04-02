package com.sts.service.impl;

import com.sts.dto.InvoiceRecordResponse;
import com.sts.mapper.InvoiceRecordMapper;
import com.sts.repository.InvoiceRecordRepository;
import com.sts.service.InvoiceRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceRecordServiceImpl implements InvoiceRecordService {


    private final InvoiceRecordRepository invoiceRecordRepository;
    private final InvoiceRecordMapper invoiceRecordMapper;

    @Transactional(readOnly = true)
    public Page<InvoiceRecordResponse> getAllInvoiceRecords(Pageable pageable) {

        var result = invoiceRecordRepository.findAll(pageable).map(invoiceRecordMapper::toResponse);

        if (result.isEmpty()) {
            log.warn("No Invoice record found - page: {}, size: {}", pageable.getPageNumber(), pageable.getOffset());
        }

        return result;
    }

}
