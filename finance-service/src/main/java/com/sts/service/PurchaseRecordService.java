package com.sts.service;

import com.sts.dto.PurchaseRecordResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PurchaseRecordService {

    Page<PurchaseRecordResponse> getAllPurchases(Pageable pageable);


}