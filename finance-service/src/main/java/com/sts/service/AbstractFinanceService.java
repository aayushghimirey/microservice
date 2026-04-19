package com.sts.service;

import com.sts.filter.TenantHolder;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractFinanceService<T, R> {

    private final RlsContext rlsContext;

    protected abstract JpaRepository<T, UUID> getRepository();

    protected abstract R toResponse(T entity);

    protected abstract String getEntityName();

    @Transactional
    public Page<R> getAllRecords(Pageable pageable) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        var response = getRepository().findAll(pageable).map(this::toResponse);
        if (response.isEmpty()) {
            log.warn("No {} record found - page: {}, size: {}",
                    getEntityName(),
                    pageable.getPageNumber(),
                    pageable.getOffset());
        }

        return response;
    }

}

