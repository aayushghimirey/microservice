package com.sts.service;

import com.sts.dto.request.CreateStaffRequest;
import com.sts.dto.response.StaffResponse;
import com.sts.filter.TenantHolder;
import com.sts.mapper.StaffMapper;
import com.sts.model.Staff;
import com.sts.repository.StaffRepository;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;

    private final RlsContext rlsContext;

    @Transactional
    public StaffResponse createStaff(CreateStaffRequest request) {

        rlsContext.with("app.tenantId", TenantHolder.getTenantId()).apply();

        Staff staff = staffMapper.toStaff(request);

        return staffMapper.toResponse(staffRepository.save(staff));
    }

    @Transactional(readOnly = true)
    public Page<StaffResponse> getAllStaff(Pageable pageable) {
        rlsContext.with("app.tenantId", TenantHolder.getTenantId()).apply();

        return staffRepository.findAll(pageable).map(staffMapper::toResponse);
    }


}
