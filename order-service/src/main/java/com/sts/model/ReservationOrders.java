package com.sts.model;

import com.sts.domain.Audit;
import com.sts.pagination.PageRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@RowLevelSecurity
@RlsRule(table = "reservation_orders", requiredVariable = "app.tenant_id", policy = "reservation_orders_tenant_policy")
public class ReservationOrders extends Audit {

    private UUID menuItemId;

    private String menuItemName;

    private BigDecimal price;

    private Integer quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

}
