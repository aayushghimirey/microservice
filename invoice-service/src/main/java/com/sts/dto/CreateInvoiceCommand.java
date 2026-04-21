package com.sts.dto;

import com.sts.enums.BillingType;
import com.sts.enums.MoneyTransaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateInvoiceCommand(
        BillingType invoiceType,
        MoneyTransaction moneyTransaction,
        BigDecimal discount,
        List<UUID> hiddenItemIds
) {


}
