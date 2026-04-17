package com.sts.utils.constant;


public interface AppConstants {

    // --- Base Paths ---
    String STOCK_BASE_PATH = "/stocks";
    String PURCHASE_BASE_PATH = "/purchases";
    String VENDOR_BASE_PATH = "/vendors";

    // --- Kafka ---
    String KAFKA_TOPIC_PURCHASE_EVENT = "purchase-event";
    String KAFKA_TOPIC_INVOICE_EVENT = "invoice-event";
    String KAFKA_TOPIC_STOCK_EVENT = "stock-event";

    // --- VAT Rate ---
    java.math.BigDecimal VAT_RATE_PERCENT = new java.math.BigDecimal("13");

    // -------------------- REQUEST LOGS --------------------
    interface Request {
        String CREATE_PURCHASE_START =
                "Request received: createPurchase itemsCount={}";
        String CREATE_PURCHASE_END =
                "Request completed: createPurchase purchaseId={}";
        String FETCH_PURCHASE_START =
                "Request received: getAllPurchases page={} size={}";
        String FETCH_PURCHASE_END =
                "Request completed: getAllPurchases page={} size={}";
        String CREATE_STOCK_START =
                "Request received: createStock stockType={}";
        String CREATE_STOCK_END =
                "Request completed: createStock stockId={}";
        String UPDATE_STOCK_START =
                "Request received: updateStock stockId={}";
        String UPDATE_STOCK_END =
                "Request completed: updateStock stockId={}";
        String CREATE_ADJUSTMENT_START = "Request received: adjustStock variantId {} ";
        String CREATE_ADJUSTMENT_END = "Request completed: adjustStock variantId {}";
        String START_VERIFY_VARIANT = "Request received: verifyVariant variantId {} unitId {}";
        String CREATE_VENDORS_START = "Request received: createVendor";
        String CREATE_VENDORS_END = "Request completed: createVendor with id {}";
    }

    // -------------------- SERVICE LOGS --------------------
    interface Logs {
        String CREATING_PURCHASE =
                "Creating purchase with invoiceNumber={}";

        String CREATING_STOCK =
                "Creating stock with name={}";

        String UPDATING_STOCK =
                "Updating stock with id={}";

        String VALIDATING_VARIANT_WITH_UNIT =
                "Validating variant with unit variantId={}, unitId={}";

        String ADJUSTING_STOCK =
                "Adjusting stock with variantId={}, unitId={}, quantity={}";


        String OUTBOX_SAVED =
                "Outbox saved for topic={}";
        String APPLICATION_EVENT_PUBLISHING =
                "Publishing application event: {}";

        String CALCULATE_STOCK_DELTA =
                "Calculating stock delta for variantId={} source={} quantity={}";

        String FETCHING_MENU_INGREDIENTS = "Fetching menu ingredients for menuId={}";

        String INVOICE_EVENT_RECEIVED = "Invoice event received invoiceId={}";

        String FAILED_TO_PROCESS_INVOICE = "Failed to process invoice with invoiceId={}";

        String PROCESSING_STOCK_UPDATE = "Processing stock update with {} items";

        String CREATING_STOCK_TRANSACTION = "Creating stock transaction for variantId={} updatedBalance={}";
    }

    // -------------------- RESPONSE MESSAGES --------------------
    interface Response {
        String PURCHASE_CREATED = "Purchase created successfully with purchaseId={}";
        String FETCHED_PURCHASES = "Fetched purchases successfully, totalRecords={}";
        String STOCK_CREATED = "Stock created successfully with stockId={}";
        String FETCHED_STOCKS = "Fetched stocks successfully, totalRecords={}";
        String STOCK_UPDATED = "Stock updated successfully with stockId={}";
        String FETCHED_STOCK_VARIANTS = "Fetched stock variants successfully, totalRecords={}";
        String STOCK_VERIFIED = "Stock verified successfully with variantId={} and unitId={}";
        String FETCHED_TRANSACTION = "Fetched stock transaction successfully, totalRecords={}";
        String VENDOR_CREATED = "Vendor created successfully with vendorId={}";
        String VENDOR_UPDATED = "Vendor updated successfully with vendorId={}";
        String FETCHED_VENDORS = "Fetched vendors successfully, totalRecords={}";
    }

    // -------------------- ERROR MESSAGES --------------------
    interface ErrorMessages {
        String VENDOR_NOT_FOUND = "Vendor not found with id=%s";
        String VARIANT_NOT_FOUND = "Variant not found with id=%s";
        String STOCK_NOT_FOUND = "Stock not found with id=%s";
        String MENU_NOT_FOUND = "Menu not found with id=%s";
        String UNIT_NOT_FOUND = "Unit not found with id=%s";
        String MENU_INTEGRATION_FAILED = "Failed to fetch menu ingredients for menuId=%s: %s";
        String INVOICE_NUMBER_EXISTS = "Invoice number already exists: %s";
        String OUTBOX_SERIALIZATION_FAILED = "Failed to serialize outbox event";
        String OUTBOX_PUBLISH_FAILED = "Failed to publish outbox event: {}";

        String STOCK_ALREADY_EXISTS = "Stock already exists with name=%s";
    }

    // ------------------ Validation Messages ------------------
    interface VALIDATION_MESSAGES {
        // CreatePurchaseCommand
        String INVOICE_NUMBER_REQUIRED = "Invoice number is required";
        String BILLING_TYPE_REQUIRED = "Billing type is required";
        String VENDOR_ID_REQUIRED = "Vendor id is required";
        String MONEY_TRANSACTION_REQUIRED = "Money transaction is required";
        String DISCOUNT_NEGATIVE = "Discount cannot be negative";
        String ITEMS_REQUIRED = "Items are required";
        String VARIANT_ID_REQUIRED = "Variant id is required";
        String UNIT_ID_REQUIRED = "Unit id is required";
        String QUANTITY_REQUIRED = "Quantity is required";
        String QUANTITY_POSITIVE = "Quantity must be positive";
        String PER_UNIT_PRICE_REQUIRED = "Per unit price is required";
        String PER_UNIT_PRICE_POSITIVE = "Per unit price must be positive";

        //CreateStockCommand
        String STOCK_NAME_REQUIRED = "Stock name is required";
        String STOCK_TYPE_REQUIRED = "Stock type is required";
        String LEAST_ONE_VARIANT_REQUIRED = "At least one variant is required";
        String VARIANT_NAME_REQUIRED = "Variant name is required";
        String BASE_UNIT_REQUIRED = "Base unit is required";
        String UNIT_NAME_REQUIRED = "Unit name is required";
        String CONVERSION_RATE_REQUIRED = "Conversion rate is required";
        String CONVERSION_RATE_POSITIVE = "Conversion rate must be positive";
        String UNIT_TYPE_REQUIRED = "Unit type is required";

        //CreateVendorCommand
        String VENDOR_NAME_REQUIRED = "Vendor name is required";
        String ADDRESS_REQUIRED = "Address is required";
        String CONTACT_NUMBER_REQUIRED = "Contact number is required";
        String PAN_NUMBER_REQUIRED = "Pan number is required";

        //StockAdjustmentCommand
        String QUANTITY_MUST_BE_POSITIVE = "Quantity must be positive";
        String REASON_TOO_LONG = "Reason is too long. Try fixing it";
    }
}