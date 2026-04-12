package com.sts.event.factory;

import com.sts.event.InvoiceEvent;
import com.sts.event.MenuIngredientResponse;
import com.sts.filter.TenantHolder;
import com.sts.model.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceEventFactory {


    public InvoiceEvent buildInvoiceEvent(Invoice invoice) {
        InvoiceEvent invoiceEvent = InvoiceEvent.builder()
                .invoiceId(invoice.getId())
                .sessionId(invoice.getSessionId())
                .reservationId(invoice.getReservationId())
                .tenantId(TenantHolder.getTenantId())
                .grossTotal(invoice.getGrossTotal())
                .reservationTime(invoice.getReservationTime())
                .reservationEndTime(invoice.getReservationEndTime()).build();

        invoice.getItems().forEach(
                item -> {
                    InvoiceEvent.InvoiceMenuItem menuItem = new InvoiceEvent.InvoiceMenuItem();
                    menuItem.setMenuId(item.getMenuItemId());
                    menuItem.setQuantity(item.getQuantity());

                    item.getIngredients().forEach(ingredient -> {
                        MenuIngredientResponse ingredientEvent = new MenuIngredientResponse();
                        ingredientEvent.setVariantId(ingredient.getVariantId());
                        ingredientEvent.setUnitId(ingredient.getUnitId());
                        ingredientEvent.setQuantity(ingredient.getQuantity());

                        menuItem.addIngredient(ingredientEvent);
                    });

                    invoiceEvent.addItem(menuItem);
                });

        return invoiceEvent;
    }

}
