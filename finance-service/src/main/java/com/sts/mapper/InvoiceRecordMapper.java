package com.sts.mapper;


import com.sts.dto.InvoiceRecordResponse;
import com.sts.event.InvoiceEvent;
import com.sts.model.InvoiceRecord;
import org.springframework.stereotype.Component;

@Component
public class InvoiceRecordMapper implements AbstractMapper<InvoiceRecord, InvoiceRecordResponse, InvoiceEvent> {

    @Override
    public InvoiceRecordResponse toResponse(InvoiceRecord entity) {
        return InvoiceRecordResponse.builder()
                .id(entity.getId())
                .invoiceId(entity.getInvoiceId())
                .grossTotal(entity.getGrossTotal())
                .reservationTime(entity.getReservationTime())
                .reservationEndTime(entity.getReservationEndTime())
                .createdDateTime(entity.getCreatedDateTime())
                .build();
    }

    @Override
    public InvoiceRecord buildEntity(InvoiceEvent event) {
        return InvoiceRecord.builder()
                .invoiceId(event.getInvoiceId())
                .reservationTime(event.getReservationTime())
                .reservationEndTime(event.getReservationEndTime())
                .grossTotal(event.getGrossTotal()).build();
    }

}
