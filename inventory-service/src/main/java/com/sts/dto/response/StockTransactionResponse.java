package com.sts.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface StockTransactionResponse {

    UUID getId();

    UUID getReferenceId();

    String getReferenceType();

    UUID getVariantId();

    String getVariantName();

    UUID getUnitId();

    String getUnitName();

    BigDecimal getQuantityChanged();

    BigDecimal getBalanceAfter();

    String getRemark();

    LocalDateTime getCreatedAt();
}