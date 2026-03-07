package com.sts.model;

import com.sts.constant.enums.InvoiceStatus;
import com.sts.domain.Audit;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Invoice extends Audit {

    private String billNumber;

    private UUID tableId;

    private UUID sessionId;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @Builder.Default
    private BigDecimal discountAmount;

    private BigDecimal subTotal;

    private BigDecimal grossTotal;

    private LocalDateTime reservationTime;

    private LocalDateTime reservationEndTime;


    public void calculateGrossTotal() {
        this.grossTotal = subTotal.subtract(discountAmount);
    }

}
