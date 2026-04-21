package com.sts.dto;

import java.math.BigDecimal;

public record FinanceServiceInfo(
        BigDecimal totalPurchaseExpense,
        BigDecimal totalInvoiceRevenue,
        BigDecimal totalVatPaid
) {
}
