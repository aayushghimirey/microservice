package com.sts.service;

import com.sts.dto.PurchaseRecordResponse;
import com.sts.mapper.PurchaseRecordMapper;
import com.sts.repository.PurchaseRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PurchaseRecordService {

    private final PurchaseRecordRepository purchaseRecordRepository;
    private final PurchaseRecordMapper purchaseRecordMapper;

    public Page<PurchaseRecordResponse> getAllPurchases(Pageable pageable) {
        return purchaseRecordRepository.findAll(pageable).map(purchaseRecordMapper::toResponse);
    }


}
