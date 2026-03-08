package com.sts.mapper;

import com.sts.dto.InvoiceResponse;
import com.sts.model.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    public InvoiceResponse toResponse(Invoice invoice) {
        return InvoiceResponse.builder()
                .billNumber(invoice.getBillNumber())
                .tableId(invoice.getTableId())
                .sessionId(invoice.getSessionId())
                .status(invoice.getStatus())
                .subTotal(invoice.getSubTotal())
                .grossTotal(invoice.getGrossTotal())
                .reservationEndTime(invoice.getReservationEndTime())
                .reservationTime(invoice.getReservationTime()).build();
    }

}
