package com.sts.service;

import com.sts.domain.BusinessDetailResponse;
import com.sts.dto.request.BusinessDetailRequest;
import com.sts.dto.request.BusinessDetailUpdate;
import com.sts.filter.TenantHolder;
import com.sts.model.BusinessDetail;
import com.sts.repository.BusinessDetailRepository;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusinessDetailService {

    private final BusinessDetailRepository businessDetailRepository;
    private final RlsContext rlsContext;

    @Transactional
    public BusinessDetailResponse createBusinessDetail(BusinessDetailRequest request) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        BusinessDetail businessDetail = BusinessDetail.builder()
                .companyName(request.getCompanyName())
                .address(request.getAddress())
                .panNumber(request.getPanNumber())
                .businessNumber(request.getBusinessNumber())
                .businessEmail(request.getBusinessEmail())
                .build();

        BusinessDetail savedDetail = businessDetailRepository.save(businessDetail);

        return BusinessDetailResponse.builder()
                .id(savedDetail.getId())
                .companyName(savedDetail.getCompanyName())
                .address(savedDetail.getAddress())
                .panNumber(savedDetail.getPanNumber())
                .businessNumber(savedDetail.getBusinessNumber())
                .businessEmail(savedDetail.getBusinessEmail())
                .build();

    }

    @Transactional
    public BusinessDetailResponse getBusinessDetail() {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();


        BusinessDetail businessDetail = businessDetailRepository.findByTenantId(TenantHolder.getTenantId())
                .orElseThrow(() -> new RuntimeException("Business detail not found"));

        return BusinessDetailResponse.builder()
                .id(businessDetail.getId())
                .companyName(businessDetail.getCompanyName())
                .address(businessDetail.getAddress())
                .panNumber(businessDetail.getPanNumber())
                .businessNumber(businessDetail.getBusinessNumber())
                .businessEmail(businessDetail.getBusinessEmail())
                .build();
    }

    @Transactional
    public BusinessDetailResponse getBusinessDetailPublic(UUID tenantId) {

        rlsContext.with("app.tenant_id", tenantId).apply();


        BusinessDetail businessDetail = businessDetailRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new RuntimeException("Business detail not found"));

        return BusinessDetailResponse.builder()
                .id(businessDetail.getId())
                .companyName(businessDetail.getCompanyName())
                .address(businessDetail.getAddress())
                .panNumber(businessDetail.getPanNumber())
                .businessNumber(businessDetail.getBusinessNumber())
                .businessEmail(businessDetail.getBusinessEmail())
                .build();
    }


    @Transactional
    public BusinessDetailResponse updateBusinessDetail(BusinessDetailUpdate request) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        BusinessDetail businessDetail = businessDetailRepository.findByTenantId(TenantHolder.getTenantId())
                .orElseThrow(() -> new RuntimeException("Business detail not found"));

        if (request.getCompanyName() != null) {
            businessDetail.setCompanyName(request.getCompanyName());
        }
        if (request.getAddress() != null) {
            businessDetail.setAddress(request.getAddress());
        }
        if (request.getPanNumber() != null) {
            businessDetail.setPanNumber(request.getPanNumber());
        }
        if (request.getBusinessNumber() != null) {
            businessDetail.setBusinessNumber(request.getBusinessNumber());
        }
        if (request.getBusinessEmail() != null) {
            businessDetail.setBusinessEmail(request.getBusinessEmail());
        }

        BusinessDetail updatedDetail = businessDetailRepository.save(businessDetail);

        return BusinessDetailResponse.builder()
                .id(updatedDetail.getId())
                .companyName(updatedDetail.getCompanyName())
                .address(updatedDetail.getAddress())
                .panNumber(updatedDetail.getPanNumber())
                .businessNumber(updatedDetail.getBusinessNumber())
                .businessEmail(updatedDetail.getBusinessEmail())
                .build();
    }

}
