package com.sts.mapper;

import com.sts.dto.request.CreateTableCommand;
import com.sts.dto.response.TableResponse;
import com.sts.model.Table;
import org.springframework.stereotype.Component;

@Component
public class TableMapper {

    public Table buildTable(CreateTableCommand request) {
        Table table = new Table();
        table.setName(request.name());
        table.setCapacity(request.capacity());
        table.setLocation(request.location());
        return table;
    }

    public TableResponse toResponse(Table entity) {
        TableResponse tableResponse = new TableResponse();
        tableResponse.setId(entity.getId());
        tableResponse.setName(entity.getName());
        tableResponse.setLocation(entity.getLocation());
        tableResponse.setCapacity(entity.getCapacity());
        return tableResponse;
    }
}
