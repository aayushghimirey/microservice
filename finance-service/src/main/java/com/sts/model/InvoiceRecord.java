package com.sts.model;


import com.sts.domain.Audit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class InvoiceRecord extends Audit {

    @Column(name = "invoice_id", nullable = false, updatable = false)
    private UUID invoiceId;

    @Builder.Default
    @Column(name = "gross_total", nullable = false, updatable = false)
    private BigDecimal grossTotal = BigDecimal.ZERO;

    @Column(name = "reservation_start_time", updatable = false, nullable = false)
    private LocalDateTime reservationTime;

    @Column(name = "reservation_end_time", updatable = false, nullable = false)
    private LocalDateTime reservationEndTime;


}
