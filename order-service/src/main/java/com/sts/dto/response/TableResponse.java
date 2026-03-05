package com.sts.dto;

import com.sts.model.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TableResponse {

    private UUID id;

    private String name;

    private String location;

    private int capacity;


    public static TableResponse fromEntity(Table entity) {
        TableResponse tableResponse = new TableResponse();
        tableResponse.setId(entity.getId());
        tableResponse.setName(entity.getName());
        tableResponse.setLocation(entity.getLocation());
        tableResponse.setCapacity(entity.getCapacity());
        return tableResponse;
    }

}
