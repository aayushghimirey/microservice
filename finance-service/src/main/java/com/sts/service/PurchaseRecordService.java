package com.sts.service;

import com.sts.dto.PurchaseRecordResponse;
import com.sts.mapper.PurchaseRecordMapper;
import com.sts.repository.PurchaseRecordRepository;
import com.sts.utils.constant.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseRecordService {

    private final PurchaseRecordRepository purchaseRecordRepository;
    private final PurchaseRecordMapper purchaseRecordMapper;

    @Transactional(readOnly = true)
    public Page<PurchaseRecordResponse> getAllPurchases(Pageable pageable) {
        var result = purchaseRecordRepository.findAll(pageable).map(purchaseRecordMapper::toResponse);

        if (result.isEmpty()) {
            log.warn("No Purchase record found - page: {}, size: {}", pageable.getPageNumber(), pageable.getOffset());
        }

        return result;
    }


}
