package com.sts.service;

import com.sts.dto.request.TableRequest;
import com.sts.model.Table;
import com.sts.repository.TableRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TableService {

    private final TableRepository tableRepository;

    @Transactional
    public Table createTable(TableRequest request) {
        Table table = new Table();
        table.setName(request.name());
        table.setCapacity(request.capacity());
        table.setLocation(request.location());

        return tableRepository.save(table);
    }

    @Transactional(readOnly = true)
    public Page<Table> getAllTables(Pageable pageable) {
        return tableRepository.findAll(pageable);
    }
}
