package com.sts.service;


import com.sts.dto.request.CreatePurchaseCommand;
import com.sts.dto.request.GetPurchaseQueryRequest;
import com.sts.dto.response.PurchaseInfo;
import com.sts.dto.response.PurchaseResponse;
import com.sts.enums.DateSelection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PurchaseService {

    PurchaseResponse createPurchase(CreatePurchaseCommand command);

    Page<PurchaseResponse> getAllPurchases(GetPurchaseQueryRequest request, Pageable pageable);

    PurchaseInfo getPurchaseInfo(DateSelection dateSelection);

}
