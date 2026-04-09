package com.sts.service;

import com.sts.filter.TenantHolder;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sts.dto.request.CreateTableCommand;
import com.sts.dto.response.TableResponse;
import com.sts.exception.DuplicateResourceException;
import com.sts.mapper.TableMapper;
import com.sts.model.Table;
import com.sts.repository.TableRepository;
import com.sts.utils.contant.AppConstants;
import com.sts.utils.enums.TableStatus;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TableService {

    private final TableRepository tableRepository;
    private final TableMapper tableMapper;
    private final RlsContext rlsContext;

    @Transactional
    public TableResponse createTable(CreateTableCommand request) {


        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        if (tableRepository.existsByName(request.name())) {
            throw new DuplicateResourceException(
                    String.format(AppConstants.ERROR_MESSAGES.DUPLICATE_TABLE_NAME, request.name()));
        }

        Table table = Table.builder()
                .name(request.name())
                .capacity(request.capacity())
                .location(request.location())
                .status(TableStatus.OPEN)
                .build();

        table = tableRepository.save(table);

        return tableMapper.toResponse(table);
    }

    @Transactional(readOnly = true)
    public Page<TableResponse> getAllTables(Pageable pageable) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        return tableRepository.findAll(pageable).map(tableMapper::toResponse);
    }
}
