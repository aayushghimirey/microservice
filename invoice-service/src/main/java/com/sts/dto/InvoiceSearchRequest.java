package com.sts.dto;

import java.util.UUID;

public record InvoiceSearchRequest (
        String billNumber,
        UUID sessionId,
        UUID tableId
) {

}
