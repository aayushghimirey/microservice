package com.sts.dto.request;

import com.sts.enums.BillingType;
import com.sts.enums.MoneyTransaction;
import com.sts.utils.constant.AppConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreatePurchaseCommand(

        @NotBlank(message = AppConstants.VALIDATION_MESSAGES.INVOICE_NUMBER_REQUIRED)
        String invoiceNumber,

        @NotNull(message = AppConstants.VALIDATION_MESSAGES.BILLING_TYPE_REQUIRED)
        BillingType billingType,

        @NotNull(message = AppConstants.VALIDATION_MESSAGES.VENDOR_ID_REQUIRED)
        UUID vendorId,

        @NotNull(message = AppConstants.VALIDATION_MESSAGES.MONEY_TRANSACTION_REQUIRED)
        MoneyTransaction moneyTransaction,

        @PositiveOrZero(message = AppConstants.VALIDATION_MESSAGES.DISCOUNT_NEGATIVE)
        BigDecimal discountAmount,

        @NotEmpty(message = AppConstants.VALIDATION_MESSAGES.ITEMS_REQUIRED)
        @Valid
        List<PurchaseItemCommand> items

) {

    public record PurchaseItemCommand(

            @NotNull(message = AppConstants.VALIDATION_MESSAGES.VARIANT_ID_REQUIRED)
            UUID variantId,

            @NotNull(message = AppConstants.VALIDATION_MESSAGES.UNIT_ID_REQUIRED)
            UUID unitId,

            @NotNull(message = AppConstants.VALIDATION_MESSAGES.QUANTITY_REQUIRED)
            @Positive(message = AppConstants.VALIDATION_MESSAGES.QUANTITY_POSITIVE)
            BigDecimal quantity,

            @NotNull(message = AppConstants.VALIDATION_MESSAGES.PER_UNIT_PRICE_REQUIRED)
            @Positive(message = AppConstants.VALIDATION_MESSAGES.PER_UNIT_PRICE_POSITIVE)
            BigDecimal perUnitPrice,

            @PositiveOrZero(message = AppConstants.VALIDATION_MESSAGES.DISCOUNT_NEGATIVE)
            BigDecimal discountAmount
    ) {
    }
}