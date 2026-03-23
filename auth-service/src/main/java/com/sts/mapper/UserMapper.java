package com.sts.mapper;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

import com.sts.dto.request.TenantRequest;
import com.sts.model.Tenant;
import com.sts.model.User;
import com.sts.utils.UserRole;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public Tenant toTenantEntity(TenantRequest request) {
        return Tenant.builder()
                .name(request.companyName())
                .email(request.email())
                .build();
    }

    public User toUserEntity(TenantRequest request, UserRole role, UUID tenantId) {
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .phoneNumber(request.adminPhone())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .tenantId(tenantId)
                .build();
        return user;
    }
}
