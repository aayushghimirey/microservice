package com.sts.dto;

import com.sts.domain.model.Menu;
import com.sts.event.MenuResponseDto;

public final class MenuResponseHandler {

    MenuResponseHandler() {
    }

    public static MenuResponseDto from(Menu menu) {
        MenuResponseDto dto = new MenuResponseDto();
        dto.setId(menu.getId());
        dto.setName(menu.getName());
        dto.setCode(menu.getCode());
        dto.setCategory(menu.getCategory().name());
        dto.setPrice(menu.getPrice());
        return dto;
    }

}
