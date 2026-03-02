package com.sts.domain.model;


import com.sts.domain.Audit;
import com.sts.domain.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
                    order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()))
            );
        }
    }

    public void addReservationOrder(ReservationOrders reservationOrder) {
        if (reservationOrder == null) return;
        reservationOrders.add(reservationOrder);
        reservationOrder.setReservation(this);
        calculateBillAmount();
    }
}