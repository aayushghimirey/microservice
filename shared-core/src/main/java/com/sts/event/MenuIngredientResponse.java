package com.sts.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuIngredientResponse {

    private UUID variantId;
    private UUID unitId;
    private double quantity;
}
