package com.sts.useCase;


import com.sts.domain.model.Table;
import com.sts.repository.TableRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TableQueryUseCase {

    private final TableRepository tableRepository;


    @Transactional(readOnly = true)
    public Page<Table> getAllTables(Pageable pageable) {
        return tableRepository.findAll(pageable);
    }
}
