//package com.sts.service;
//
//import com.sts.dto.request.BusinessDetailRequest;
//import com.sts.dto.response.BusinessDetailResponse;
//import com.sts.repository.BusinessDetailRepository;
//import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class BusinessDetailService {
//
//    private final BusinessDetailRepository businessDetailRepository;
//    private final RlsContext rlsContext;
//
//    @Transactional
//    public BusinessDetailResponse createBusinessDetail(BusinessDetailRequest request) {
//
//        rlsContext.with("app.tenant_id", request.getTenantId());
//
//
//
//    }
//
//}
