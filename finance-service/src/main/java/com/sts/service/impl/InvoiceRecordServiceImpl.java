package com.sts.service.impl;

import com.sts.dto.InvoiceRecordResponse;
import com.sts.mapper.InvoiceRecordMapper;
import com.sts.model.InvoiceRecord;
import com.sts.repository.InvoiceRecordRepository;
import com.sts.service.AbstractFinanceService;
import com.sts.service.interfaces.InvoiceRecordService;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service

@Slf4j
public class InvoiceRecordServiceImpl extends AbstractFinanceService<InvoiceRecord, InvoiceRecordResponse> implements InvoiceRecordService {


    private final InvoiceRecordRepository invoiceRecordRepository;
    private final InvoiceRecordMapper invoiceRecordMapper;

    public InvoiceRecordServiceImpl(RlsContext rlsContext, InvoiceRecordRepository invoiceRecordRepository, InvoiceRecordMapper mapper) {
        super(rlsContext);
        this.invoiceRecordMapper = mapper;
        this.invoiceRecordRepository = invoiceRecordRepository;
    }


    @Override
    protected JpaRepository<InvoiceRecord, UUID> getRepository() {
        return invoiceRecordRepository;
    }

    @Override
    protected InvoiceRecordResponse toResponse(InvoiceRecord entity) {
        return invoiceRecordMapper.toResponse(entity);
    }

    @Override
    protected String getEntityName() {
        return "InvoiceRecord";
    }

}
