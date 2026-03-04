package com.sts.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MenuResponseDto {

    private UUID id;
    private String name;
    private String code;
    private String category;
    private BigDecimal price;


}
