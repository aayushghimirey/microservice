package com.sts.utils.constant;

public interface AppConstants {

    String INVOICE_BASE_PATH = "/invoices";

    String KAFKA_TOPIC_INVOICE_EVENT = "invoice-event";

    interface SUCCESS_MESSAGES {
        String INVOICES_FETCHED = "Invoices fetched successfully";
        String INVOICE_CREATED = "Invoice created successfully";
    }

    interface ERROR_MESSAGES {
        String INVOICE_NOT_FOUND = "Invoice not found with id: %s";
        String OUTBOX_SERIALIZATION_FAILED = "Failed to serialize data";
        String OUTBOX_PUBLISH_FAILED = "Failed to process Outbox payload";
    }

    interface LOG_MESSAGES {
        String ORDER_EVENT_MESSAGE = "Order event received with session id {}";
        String INVOICE_PROCESSED_SUCCESS = "Invoice processed successfully for session '%s'";
        String PROCESSING_INVOICE = "Processing invoice with id: {}";
        String INVOICE_PAID = "Invoice marked as PAID for id: {}";
    }

}
