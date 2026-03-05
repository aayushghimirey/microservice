package com.sts.service;

import com.sts.dto.request.CreateTableCommand;
import com.sts.dto.response.TableResponse;
import com.sts.mapper.TableMapper;
import com.sts.model.Table;
import com.sts.repository.TableRepository;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.SQLJoinTableRestriction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TableService {

    private final TableRepository tableRepository;
    private final TableMapper tableMapper;

    @Transactional
    public TableResponse createTable(CreateTableCommand request) {
        Table table = new Table();
        table.setName(request.name());
        table.setCapacity(request.capacity());
        table.setLocation(request.location());

        return tableMapper.toResponse(tableRepository.save(table));
    }

    @Transactional(readOnly = true)
    public Page<TableResponse> getAllTables(Pageable pageable) {
        return tableRepository.findAll(pageable).map(tableMapper::toResponse);
    }
}
