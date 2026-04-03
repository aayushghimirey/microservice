package com.sts.model;


import com.sts.domain.Audit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
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
@Table(name = "invoice_record")
public class InvoiceRecord extends Audit {

    @Column(name = "invoice_id", nullable = false, updatable = false, unique = true)
    private UUID invoiceId;

    @Builder.Default
    @Column(name = "gross_total", nullable = false)
    private BigDecimal grossTotal = BigDecimal.ZERO;

    @Column(name = "reservation_start_time", updatable = false, nullable = false)
    private LocalDateTime reservationTime;

    @Column(name = "reservation_end_time", updatable = false, nullable = false)
    private LocalDateTime reservationEndTime;


}
