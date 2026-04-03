package com.sts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Slf4j
public abstract class AbstractFinanceService<T, R> {

    protected abstract JpaRepository<T, UUID> getRepository();

    protected abstract R toResponse(T entity);

    protected abstract String getEntityName();

    public Page<R> getAllRecords(Pageable pageable) {
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

