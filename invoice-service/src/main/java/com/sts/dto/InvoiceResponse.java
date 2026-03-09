package com.sts.dto;

import com.sts.utils.enums.InvoiceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {

    private UUID id;

    private String billNumber;

    private UUID tableId;

    private UUID sessionId;

    private InvoiceStatus status;

    private BigDecimal subTotal = BigDecimal.ZERO;

    private BigDecimal grossTotal = BigDecimal.ZERO;

    private LocalDateTime reservationTime;

    private LocalDateTime reservationEndTime;

}
