package com.sts.model;


import com.sts.domain.Audit;
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

    private UUID invoiceId;

    @Builder.Default
    private BigDecimal grossTotal = BigDecimal.ZERO;

    private LocalDateTime reservationTime;

    private LocalDateTime reservationEndTime;


}
