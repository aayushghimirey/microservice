package com.sts.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponse {

    private UUID id;
    private String name;
    private String code;
    private String category;
    private BigDecimal price;


}
