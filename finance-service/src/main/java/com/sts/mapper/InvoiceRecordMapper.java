package com.sts.mapper;


import com.sts.dto.InvoiceRecordResponse;
import com.sts.event.InvoiceEvent;
import com.sts.model.InvoiceRecord;
import org.springframework.stereotype.Component;

@Component
public class InvoiceRecordMapper {

    public InvoiceRecordResponse toResponse(InvoiceRecord record) {
        return InvoiceRecordResponse.builder()
                .invoiceId(record.getInvoiceId())
                .grossTotal(record.getGrossTotal())
                .reservationTime(record.getReservationTime())
                .reservationEndTime(record.getReservationEndTime())
                .build();
    }

    public InvoiceRecord buildInvoiceRecord(InvoiceEvent event) {
        return InvoiceRecord.builder()
                .invoiceId(event.getInvoiceId())
                .reservationTime(event.getReservationTime())
                .reservationEndTime(event.getReservationEndTime())
                .grossTotal(event.getGrossTotal()).build();
    }

}
