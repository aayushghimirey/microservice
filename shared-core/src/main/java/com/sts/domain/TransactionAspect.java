package com.sts.domain;

import com.sts.filter.TenantHolder;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class TransactionAspect {

    @Autowired
    private RlsContext rlsContext;

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void beforeTransaction(JoinPoint joinPoint) {

        UUID tenant = TenantHolder.getTenantId();

        if (tenant == null) {
            throw new IllegalStateException("Tenant not set before transaction");
        }

        try {
            rlsContext.with("app.tenant_id", tenant).apply();
        } finally {
            TenantHolder.clear();
        }

    }
}