package com.sts.utils.contant;


public interface AppConstants {

    String STOCK_BASE_PATH = "/stocks";
    String PURCHASE_BASE_PATH = "/purchases";
    String VENDOR_BASE_PATH = "/vendors";

    interface SUCCESS_MESSAGES {
        String PURCHASE_CREATED = "Purchase created successfully";
        String PURCHASE_FETCHED = "Purchases fetched successfully";
        String STOCK_CREATED = "Stock created successfully";
        String STOCK_UPDATED = "Stock updated successfully";
        String STOCKS_FETCHED = "Stocks fetched successfully";
        String VARIANTS_FETCHED = "Variants fetched successfully";
        String TRANSACTIONS_FETCHED = "Transactions fetched successfully";
    }

    interface ERROR_MESSAGES {
        String VARIANT_NOT_FOUND = "Variant not found with id '%s'";
        String UNIT_NOT_FOUND = "Unit not found with id '%s'";
        String INVOICE_NUMBER_EXISTS = "Purchase with invoice number '%s' already exists";
        String STOCK_ALREADY_EXISTS = "Stock already exists with name '%s'";
        String VENDOR_NOT_FOUND = " Vendor not found with id '%s'";
        String STOCK_NOT_FOUND = "Stock not found with id '%s'";
        String INVALID_UNIT_TYPE = "Invalid unit type '%s' for purchase";
    }

    interface LOG_MESSAGES {
        String CREATING_PURCHASE = "Creating purchase with invoice: {}";
        String PURCHASE_CREATED = "Purchase created successfully with id: {}";
        String CREATING_STOCK = "Creating stock with name: {}";
        String STOCK_CREATED = "Stock created successfully with id: {}";
        String UPDATING_STOCK = "Updating stock with id: {}";
        String CREATING_ADJUSTMENT = "Creating adjustment for variant: {}, unit: {}, quantity: {}";
        String VALIDATING_VARIANT = "Validating variantId {} with unitId {}";

    }

    interface VALIDATION_MESSAGES {
        String STOCK_NAME_REQUIRED = "Stock name is required";
        String STOCK_TYPE_REQUIRED = "Stock type is required";
        String LEAST_ONE_VARIANT_REQUIRED = "At least one variant is required";

        String VARIANT_NAME_REQUIRED = "Variant name is required";
        String BASE_UNIT_REQUIRED = "Base unit is required";

        String UNIT_NAME_REQUIRED = "Unit name is required";
        String CONVERSION_RATE_REQUIRED = "Conversion rate is required";
        String UNIT_TYPE_REQUIRED = "Unit type is required";

        String QUANTITY_MUST_BE_POSITIVE = "Quantity must be greater than zero";
        String VARIANT_ID_REQUIRED = "Variant id is required";
        String UNIT_ID_REQUIRED = "Unit id is required";
        String REASON_TOO_LONG = "Reason cannot exceed 255 characters";
        String QUANTITY_REQUIRED = "Quantity is required";


        String INVOICE_NUMBER_REQUIRED = "Invoice number is required";
        String BILLING_TYPE_REQUIRED = "Billing type is required";
        String MONEY_TRANSACTION_REQUIRED = "Money transaction type is required";

        String VENDOR_ID_REQUIRED = "Vendor id is required";

        String ITEMS_REQUIRED = "At least one purchase item is required";

        String QUANTITY_POSITIVE = "Quantity must be greater than zero";

        String PER_UNIT_PRICE_REQUIRED = "Per unit price is required";
        String PER_UNIT_PRICE_POSITIVE = "Per unit price must be greater than zero";

        String DISCOUNT_NEGATIVE = "Discount amount cannot be negative";

    }

}
