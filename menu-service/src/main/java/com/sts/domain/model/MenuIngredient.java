package com.sts.domain.model;

import com.sts.domain.Audit;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(EntityListeners.class)
@Builder
@Table(name = "menu_ingredient")
public class MenuIngredient extends Audit {


    @Column(name = "variant_id", nullable = false)
    private UUID variantId;

    @Column(name = "unit_id", nullable = false)
    private UUID unitId;

    @Column(name = "quantity", nullable = false)
    private double quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

}
