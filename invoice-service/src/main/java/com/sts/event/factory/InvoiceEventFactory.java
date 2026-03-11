package com.sts.event.factory;

import com.sts.event.InvoiceEvent;
import com.sts.model.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceEventFactory {


    public InvoiceEvent buildInvoiceEvent(Invoice invoice) {
        InvoiceEvent invoiceEvent = InvoiceEvent.builder()
                .invoiceId(invoice.getId())
                .sessionId(invoice.getSessionId())
                .grossTotal(invoice.getGrossTotal())
                .reservationTime(invoice.getReservationTime())
                .reservationEndTime(invoice.getReservationEndTime()).build();

        invoice.getItems().forEach(
                item -> {
                    InvoiceEvent.InvoiceMenuItem menuItem = new InvoiceEvent.InvoiceMenuItem();
                    menuItem.setMenuId(item.getMenuItemId());
                    menuItem.setQuantity(item.getQuantity());

                    invoiceEvent.addItem(menuItem);
                });

        return invoiceEvent;
    }

}
