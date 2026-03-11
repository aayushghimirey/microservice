package com.sts.service;


import com.sts.dto.request.CreatePurchaseCommand;
import com.sts.dto.response.PurchaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PurchaseService {

    PurchaseResponse createPurchase(CreatePurchaseCommand command);

    Page<PurchaseResponse> getAllPurchases(Pageable pageable);


}
