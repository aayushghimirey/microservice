package com.sts.dto;

import com.sts.utils.enums.MenuCategory;

public record MenuQueryDto(
        MenuCategory category,
        String menuName,
        String code
) {
}
