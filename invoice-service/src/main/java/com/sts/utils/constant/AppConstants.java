package com.sts.utils.constant;


public interface AppConstants {

    String INVOICE_BASE_PATH = "/invoices";

    interface SUCCESS_MESSAGES {
        String INVOICES_FETCHED = "Invoices fetched successfully";
        String INVOICE_CREATED = " Invoice created successfully";
    }

    interface LOG_MESSAGES {
        String ORDER_EVENT_MESSAGE = "Order event received '%s'";
        String INVOICE_PROCESSED_SUCCESS = "Invoice processed successfully for session '%s'";
    }

}
