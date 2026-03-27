package com.sts.model;

import com.sts.domain.Audit;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)

public class UnitSnapshot extends Audit {
    UUID unitId;
    String name;
    BigDecimal conversionRate;
    String unitType;

    @ManyToOne
    @JoinColumn(name = "variant_snapshot_id")
    private VariantSnapshot variantSnapshot;
}
