package com.sts.event.strategy;

import com.sts.event.EventProcessingStrategy;
import com.sts.event.InvoiceEvent;
import com.sts.mapper.InvoiceRecordMapper;
import com.sts.repository.InvoiceRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class InvoiceEventProcessingStrategy implements EventProcessingStrategy<InvoiceEvent> {

    private final InvoiceRecordRepository invoiceRecordRepository;
    private final InvoiceRecordMapper invoiceRecordMapper;

    @Override
    public void process(InvoiceEvent event) {
        if (invoiceRecordRepository.findByInvoiceId(event.getInvoiceId()).isPresent()) {
            log.warn("Duplicate invoice id found - {}", event.getInvoiceId());
        }
        invoiceRecordRepository.save(invoiceRecordMapper.buildRecord(event));
    }
}
