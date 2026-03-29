package com.sts.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class MenuIngredientResponse {

    private UUID variantId;
    private UUID unitId;
    private double quantity;

}
