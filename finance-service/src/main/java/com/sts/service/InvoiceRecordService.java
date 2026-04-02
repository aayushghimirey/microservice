package com.sts.service;

import com.sts.dto.InvoiceRecordResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface InvoiceRecordService {
       Page<InvoiceRecordResponse> getAllInvoiceRecords(Pageable pageable) ;
}
