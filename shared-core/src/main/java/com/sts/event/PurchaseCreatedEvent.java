package com.sts.event;

import com.sts.enums.BillingType;
import com.sts.enums.MoneyTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseCreatedEvent {
    private UUID purchaseId;
    private BillingType billingType;
    private MoneyTransaction moneyTransaction;
    private BigDecimal vatAmount;
    private BigDecimal grossTotal;
}
