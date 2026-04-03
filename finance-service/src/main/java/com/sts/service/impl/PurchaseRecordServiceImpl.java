package com.sts.service.impl;

import com.sts.dto.PurchaseRecordResponse;
import com.sts.mapper.PurchaseRecordMapper;
import com.sts.model.PurchaseRecord;
import com.sts.repository.PurchaseRecordRepository;
import com.sts.service.AbstractFinanceService;
import com.sts.service.interfaces.PurchaseRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseRecordServiceImpl extends AbstractFinanceService<PurchaseRecord, PurchaseRecordResponse> implements PurchaseRecordService {

    private final PurchaseRecordRepository purchaseRecordRepository;
    private final PurchaseRecordMapper purchaseRecordMapper;


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
