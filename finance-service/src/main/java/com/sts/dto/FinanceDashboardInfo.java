package com.sts.dto;

import java.math.BigDecimal;

public record FinanceDashboardInfo(
        BigDecimal totalPurchaseExpense,
        BigDecimal totalInvoiceRevenue,
        BigDecimal totalVatPaid
) {
}
