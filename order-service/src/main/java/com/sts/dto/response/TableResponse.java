package com.sts.dto.response;

import com.sts.model.Table;
import com.sts.utils.enums.TableStatus;
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

    private TableStatus status;


}
