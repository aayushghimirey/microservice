package com.sts.service;

import com.sts.dto.request.TenantRequest;
import com.sts.mapper.UserMapper;
import com.sts.model.Tenant;
import com.sts.model.User;
import com.sts.repository.TenantRepository;
import com.sts.repository.UserRepository;
import com.sts.utils.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

//    @Transactional
    public void registerTenant(TenantRequest request) {

        final Tenant savedTenant = this.saveTenant(userMapper.toTenantEntity(request));

        final User adminUser = userMapper.toUserEntity(request, UserRole.TENANT, savedTenant.getId());

        userRepository.save(adminUser);

    }

    private Tenant saveTenant(Tenant tenant) {
        duplicateTenantValidator(tenant.getName(), tenant.getEmail());
        return tenantRepository.save(tenant);
    }


    private void duplicateTenantValidator(String companyName, String email) {
        if (tenantRepository.existsByName(companyName)) {
            throw new DuplicateKeyException("Tenant with name " + companyName + " already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateKeyException("User with email " + email + " already exists");
        }
    }

}
