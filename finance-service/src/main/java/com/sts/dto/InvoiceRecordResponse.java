package com.sts.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record InvoiceRecordResponse(
        UUID id,
        UUID invoiceId,
        BigDecimal grossTotal,
        LocalDateTime reservationTime,
        LocalDateTime reservationEndTime,
        LocalDateTime createdDateTime
) {
}
