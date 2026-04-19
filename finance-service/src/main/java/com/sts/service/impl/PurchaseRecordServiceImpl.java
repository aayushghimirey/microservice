package com.sts.service.impl;

import com.sts.dto.PurchaseRecordResponse;
import com.sts.mapper.InvoiceRecordMapper;
import com.sts.mapper.PurchaseRecordMapper;
import com.sts.model.PurchaseRecord;
import com.sts.repository.InvoiceRecordRepository;
import com.sts.repository.PurchaseRecordRepository;
import com.sts.service.AbstractFinanceService;
import com.sts.service.interfaces.PurchaseRecordService;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PurchaseRecordServiceImpl extends AbstractFinanceService<PurchaseRecord, PurchaseRecordResponse> implements PurchaseRecordService {

    private final PurchaseRecordRepository purchaseRecordRepository;
    private final PurchaseRecordMapper purchaseRecordMapper;

    public PurchaseRecordServiceImpl(RlsContext rlsContext, PurchaseRecordRepository purchaseRecordRepository, PurchaseRecordMapper mapper) {
        super(rlsContext);
        this.purchaseRecordMapper = mapper;
        this.purchaseRecordRepository = purchaseRecordRepository;
    }


    @Override
    protected JpaRepository<PurchaseRecord, UUID> getRepository() {
        return purchaseRecordRepository;
    }

    @Override
    protected PurchaseRecordResponse toResponse(PurchaseRecord entity) {
        return purchaseRecordMapper.toResponse(entity);
    }

    @Override
    protected String getEntityName() {
        return "PurchaseRecord";
    }
}
