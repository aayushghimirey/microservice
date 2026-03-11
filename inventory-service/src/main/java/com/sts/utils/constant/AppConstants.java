package com.sts.utils.constant;

public interface AppConstants {

    String STOCK_BASE_PATH = "/stocks";
    String PURCHASE_BASE_PATH = "/purchases";
    String VENDOR_BASE_PATH = "/vendors";

    String KAFKA_TOPIC_PURCHASE_EVENT = "purchase-event";

    java.math.BigDecimal VAT_RATE_PERCENT = new java.math.BigDecimal("13");

    interface SUCCESS_MESSAGES {
        String PURCHASE_CREATED = "Purchase created successfully";
        String PURCHASE_FETCHED = "Purchases fetched successfully";
        String STOCK_CREATED = "Stock created successfully";
        String STOCK_UPDATED = "Stock updated successfully";
        String STOCKS_FETCHED = "Stocks fetched successfully";
        String VARIANTS_FETCHED = "Variants fetched successfully";
        String STOCK_VERIFIED = "Stock verification completed";
        String TRANSACTIONS_FETCHED = "Transactions fetched successfully";
        String VENDOR_CREATED = "Vendor created successfully";
        String VENDORS_FETCHED = "Vendors fetched successfully";
    }

    interface ERROR_MESSAGES {
        String VARIANT_NOT_FOUND = "Variant not found with id '%s'";
        String UNIT_NOT_FOUND = "Unit not found with id '%s'";
        String INVOICE_NUMBER_EXISTS = "Purchase with invoice number '%s' already exists";
        String STOCK_ALREADY_EXISTS = "Stock already exists with name '%s'";
        String VENDOR_NOT_FOUND = "Vendor not found with id '%s'";
        String STOCK_NOT_FOUND = "Stock not found with id '%s'";
        String INVALID_UNIT_TYPE = "Invalid unit type '%s' for purchase";
        String EVENT_SERIALIZATION_FAILED = "Error while serialization";
        String OUTBOX_SERIALIZATION_FAILED = "Failed to serialize data";
        String OUTBOX_PUBLISH_FAILED = "Failed to process Outbox payload";
        String MENU_NOT_FOUND = "Menu not found or has no ingredients for menuId '%s'";
        String MENU_INTEGRATION_FAILED = "Error fetching menu ingredients for menuId '%s': %s";
    }

    interface LOG_MESSAGES {
        String CREATING_PURCHASE = "Creating purchase with invoice: {}";
        String FETCHING_PURCHASE = "Fetching purchase with in page {} and size {}";
        String CREATING_STOCK = "Creating stock with name: {}";
        String APPLICATION_EVENT_PUBLISHING = "Publishing event type {}";
        String UPDATING_STOCK = "Updating stock with id: {}";
        String CREATING_ADJUSTMENT = "Creating adjustment for variant: {}, unit: {}, quantity: {}";
        String VALIDATING_VARIANT = "Validating variantId {} with unitId {}";
        String CREATING_VENDOR = "Creating vendor: {}";
        String VENDOR_CREATED = "Vendor created successfully: {}";
        String FAILED_TO_PROCESS_INVOICE = "Failed to process invoice event invoiceId {}";
        String INVOICE_EVENT_RECEIVED = "Invoice event received with id {}";
        String FETCHING_MENU_INGREDIENTS = "Fetching ingredients for menuId={}";
        String PROCESSING_STOCK_UPDATE = "Processing StockUpdateEvent with {} items";
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
