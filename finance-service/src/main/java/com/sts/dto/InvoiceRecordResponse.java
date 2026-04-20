package com.sts.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record InvoiceRecordResponse(
        UUID id,
        UUID invoiceId,
        BigDecimal grossTotal,
        Instant reservationTime,
        Instant reservationEndTime,
        Instant createdDateTime
) {
}
