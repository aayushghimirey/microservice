package com.sts.domain;

import com.sts.filter.TenantHolder;
import jakarta.persistence.PrePersist;

import java.util.UUID;

public class AuditListener {

    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof Audit audit) {

            // set tenant
            UUID tenant = TenantHolder.getTenantId();
            if (tenant == null) {
                throw new IllegalStateException("Tenant missing");
            }

            audit.setTenantId(tenant);
        }
    }

}
