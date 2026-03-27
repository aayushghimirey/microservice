package com.sts.model;

import com.sts.domain.Audit;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)

public class VariantSnapshot extends Audit {
    UUID variantId;
    String name;
    String baseUnit;
    BigDecimal openingStock;
    BigDecimal currentStock;

    @OneToMany(mappedBy = "variantSnapshot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<UnitSnapshot> units = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "stock_snapshot_id")
    private StockSnapshot stockSnapshot;

    public void addUnit(UnitSnapshot unit) {
        if (unit == null) return;
        this.units.add(unit);
    }


}
