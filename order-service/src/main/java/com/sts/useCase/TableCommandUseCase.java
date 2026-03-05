package com.sts.useCase;

import com.sts.command.CreateTableCommand;
import com.sts.domain.model.Table;
import com.sts.repository.TableRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TableCommandUseCase {

    private final TableRepository tableRepository;


    @Transactional
    public Table createTable(CreateTableCommand command) {
        Table table = new Table();
        table.setName(command.name());
        table.setCapacity(command.capacity());
        table.setLocation(command.location());

        return tableRepository.save(table);
    }

}
