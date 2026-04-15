package com.sts.event.strategy;

import com.sts.event.InvoiceEvent;
import com.sts.filter.TenantHolder;
import com.sts.mapper.InvoiceRecordMapper;
import com.sts.model.InvoiceRecord;
import com.sts.repository.InvoiceRecordRepository;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Slf4j
@RequiredArgsConstructor
public class InvoiceEventProcessingStrategy extends AbstractEventProcessingStrategy<InvoiceEvent> {

    private final InvoiceRecordRepository invoiceRecordRepository;
    private final InvoiceRecordMapper invoiceRecordMapper;
    private final RlsContext rlsContext;


    @Override
    @Transactional
    protected void save(InvoiceEvent event) {
        TenantHolder.setTenantId(event.getTenantId());
        rlsContext.with("app.tenant_id", event.getTenantId()).apply();

        invoiceRecordRepository.findByInvoiceId(event.getInvoiceId())
                .ifPresentOrElse(
                        existing -> update(existing, event),
                        () -> insert(event)
                );
    }

    // helpers
    private void update(InvoiceRecord existing, InvoiceEvent event) {
        existing.setGrossTotal(event.getGrossTotal());
        log.info("Invoice record updated - invoiceId: {}", event.getInvoiceId());
    }

    private void insert(InvoiceEvent event) {
        invoiceRecordRepository.save(invoiceRecordMapper.buildEntity(event));
        log.info("Invoice record inserted - invoiceId: {}", event.getInvoiceId());
    }

}
