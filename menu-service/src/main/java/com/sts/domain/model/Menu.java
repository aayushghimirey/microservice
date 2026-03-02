package com.sts.domain.model;


import com.sts.domain.Audit;
import com.sts.domain.enums.MenuCategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(EntityListeners.class)
@Builder
@Table(name = "menu")
public class Menu extends Audit {

    @Column(name = "name", nullable = false)
    private String name;

    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private MenuCategory category;

    @Column(name = "price", nullable = false)
    private BigDecimal price;


    @OneToMany(mappedBy = "menu", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MenuIngredient> ingredients = new ArrayList<>();

    public void addIngredient(MenuIngredient ingredient) {
        if (ingredient == null) return;
        ingredients.add(ingredient);
        ingredient.setMenu(this);
    }

}
