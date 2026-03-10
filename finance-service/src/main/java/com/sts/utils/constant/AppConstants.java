package com.sts.utils.constant;

public interface AppConstants {

    interface LOG_MESSAGES {
        String INVOICE_EVENT_RECEIVED = "Invoice event received with invoice id: {}";
        String INVOICE_RECORD_SAVED = "Invoice record saved for invoiceId: {}";
        String INVOICE_EVENT_FAILED = "Failed to process invoice event for invoiceId: {}";
        String PURCHASE_EVENT_RECEIVED = "Purchase event received with purchase id {}";
        String PURCHASE_RECORD_SAVED = "Purchase record saved for purchaseId: {}";
        String PURCHASE_EVENT_FAILED = "Failed to process purchase event at purchase id: {}";
    }
}
