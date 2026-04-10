package com.sts.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sts.domain.Audit;
import com.sts.utils.enums.ReservationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RowLevelSecurity
@RlsRule(table = "reservation", requiredVariable = "app.tenant_id", policy = "reservation_tenant_policy")
@EntityListeners(AuditingEntityListener.class)
public class Reservation extends Audit {

    private UUID sessionId;

    @Column(nullable = false)
    private LocalDateTime reservationTime;

    private LocalDateTime reservationEndTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Builder.Default
    private BigDecimal billAmount = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private Table table;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReservationOrders> reservationOrders = new ArrayList<>();

    public void calculateBillAmount() {
        this.billAmount = BigDecimal.ZERO;
        for (var order : reservationOrders) {
            this.billAmount = this.billAmount.add(
                    order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));
        }
    }

    public void addReservationOrder(ReservationOrders reservationOrder) {
        if (reservationOrder == null)
            return;
        reservationOrders.add(reservationOrder);
        reservationOrder.setReservation(this);
        calculateBillAmount();
    }
}