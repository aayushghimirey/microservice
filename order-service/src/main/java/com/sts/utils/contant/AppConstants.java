package com.sts.utils.contant;

import java.util.UUID;

public interface AppConstants {

    UUID TEST_TENANT = UUID.fromString("11111111-1111-1111-1111-111111111111");
    String ORDER_KAFKA_EVENT_TOPIC = "order-event";

    String TABLE_BASE_PATH = "/tables";
    String RESERVATION_BASE_PATH = "/reservations";

    interface SUCCESS_MESSAGES {
        String RESERVATION_CREATED = "Reservation Created";
        String RESERVATION_FETCHED = "Reservation fetch successfully";
        String TABLE_CREATED = "Table create successfully";
        String TABLES_FETCHED = "Tables fetched successfully";
    }

    interface ERROR_MESSAGES {
        String MENU_NOT_FOUND = "Menu not found with id '%s'";
        String TABLE_NOT_FOUND = "Table not found with id '%s'";
        String TABLE_NOT_OPEN = "Table must be OPEN to proceed";
        String DUPLICATE_TABLE_NAME = "Table with name '%s' already exists";
        String ORDER_PUBLISH_FAILED = "Error publishing order created event";
    }

    interface LOG_MESSAGES {
        String CREATING_RESERVATION = "Creating reservation for table: {}";
        String RESERVATION_CREATED = "Reservation created with id: {}";
        String CREATING_TABLE = "Creating table: {}";
        String TABLE_CREATED = "Table created with id: {}";
        String INVOICE_EVENT_RECEIVED = "Invoice event received with id: {}";
        String INVOICE_EVENT_FAILED = "Failed to process invoice event for id: {}";
        String SESSION_NOT_FOUND = "No reservation found for session: {}";
        String RESERVATION_COMPLETED = "Reservation completed for session: {}";
        String OUTBOX_EVENT_FAILED = "Failed to create outbox event for reservation: {}";
    }

}
