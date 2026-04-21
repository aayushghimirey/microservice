package com.sts.mapper;

import com.sts.dto.InvoiceResponse;
import com.sts.model.Invoice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InvoiceMapper {

    public InvoiceResponse toResponse(Invoice invoice) {
        List<InvoiceResponse.InvoiceItemResponse> items =
                invoice.getItems().stream()
                        .map(item -> {
                            InvoiceResponse.InvoiceItemResponse r =
                                    new InvoiceResponse.InvoiceItemResponse();
                            r.setId(item.getId());
                            r.setName(item.getName());
                            r.setPrice(item.getPrice());
                            r.setQuantity(item.getQuantity());
                            return r;
                        })
                        .toList();

        return InvoiceResponse.builder()
                .id(invoice.getId())
                .billNumber(invoice.getBillNumber())
                .tableId(invoice.getTableId())
                .sessionId(invoice.getSessionId())
                .status(invoice.getStatus())
                .subTotal(invoice.getSubTotal())
                .grossTotal(invoice.getGrossTotal())
                .reservationEndTime(invoice.getReservationEndTime())
                .reservationTime(invoice.getReservationTime())
                .items(new ArrayList<>(items))
                .build();

    }

}
