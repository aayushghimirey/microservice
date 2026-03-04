package com.sts.model;

import com.sts.domain.Audit;
import com.sts.utils.enums.MenuCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity representing a Menu item.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "menu")
@EntityListeners(AuditingEntityListener.class)
public class Menu extends Audit {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private MenuCategory category;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MenuIngredient> ingredients = new ArrayList<>();

    /**
     * Adds an ingredient and maintains the bidirectional relationship.
     */
    public void addIngredient(MenuIngredient ingredient) {
        if (ingredient == null)
            return;
        ingredients.add(ingredient);
        ingredient.setMenu(this);
    }
}
