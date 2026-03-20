package com.sts.utils.constant;


public interface AppConstants {

    // --- Base Paths ---
    String STOCK_BASE_PATH = "/stocks";
    String PURCHASE_BASE_PATH = "/purchases";
    String VENDOR_BASE_PATH = "/vendors";

    // --- Kafka ---
    String KAFKA_TOPIC_PURCHASE_EVENT = "purchase-event";

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
    }

    // -------------------- SERVICE LOGS --------------------
    interface Logs {
        String CREATING_PURCHASE =
                "Creating purchase with invoiceNumber={}";
        String RESOLVING_VENDOR =
                "Resolving vendor with id={}";
        String VALIDATING_PURCHASE_ITEMS =
                "Validating purchase items count={}";
        String PUBLISHING_STOCK_UPDATE_EVENT =
                "Publishing stock update event for purchaseId={}";
        String SAVING_PURCHASE_OUTBOX_EVENT =
                "Saving purchase outbox event for purchaseId={}";
        String FETCHING_PURCHASE =
                "Fetching purchases page={} size={}";
        String OUTBOX_SAVED =
                "Outbox saved for topic={}";
        String APPLICATION_EVENT_PUBLISHING =
                "Publishing application event: {}";
    }

    // -------------------- RESPONSE MESSAGES --------------------
    interface Response {
        String PURCHASE_CREATED = "Purchase created successfully with purchaseId={}";
        String FETCHED_PURCHASES = "Fetched purchases successfully, totalRecords={}";
    }

    // -------------------- ERROR MESSAGES --------------------
    interface ErrorMessages {
        String VENDOR_NOT_FOUND = "Vendor not found with id=%s";
        String VARIANT_NOT_FOUND = "Variant not found with id=%s";
        String STOCK_NOT_FOUND = "Stock not found with id=%s";
        String UNIT_NOT_FOUND = "Unit not found with id=%s";
        String INVOICE_NUMBER_EXISTS = "Invoice number already exists: %s";
        String OUTBOX_SERIALIZATION_FAILED = "Failed to serialize outbox event";
        String OUTBOX_PUBLISH_FAILED = "Failed to publish outbox event: {}";
    }
}