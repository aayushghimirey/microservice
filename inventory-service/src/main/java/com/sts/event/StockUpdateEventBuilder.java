package com.sts.event;


import com.sts.model.purchase.Purchase;
import com.sts.utils.enums.StockUpdateSource;
import com.sts.utils.feign.MenuClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * Application event for updating stock items after purchase / adjustment
 * */
@Component
@AllArgsConstructor
public class StockUpdateEventBuilder {

    private static final Logger log = LoggerFactory.getLogger(StockUpdateEventBuilder.class);
    private final MenuClient menuClient;

    public StockUpdateEvent buildFromPurchase(Purchase purchase) {

        var infos = purchase.getPurchaseItems()
                .stream()
                .map(item -> new StockUpdateEvent.Info(
                        item.getVariantId(),
                        item.getUnitId(),
                        item.getQuantity(),
                        StockUpdateSource.PURCHASE
                ))
                .toList();

        return new StockUpdateEvent(purchase.getId(), null, infos);
    }


    public StockUpdateEvent buildFromAdjustment(
            UUID variantId,
            UUID unitId,
            BigDecimal quantity
    ) {

        var info = new StockUpdateEvent.Info(
                variantId,
                unitId,
                quantity,
                StockUpdateSource.ADJUSTMENT
        );

        return new StockUpdateEvent(
                null,
                null,
                List.of(info)
        );
    }

    public StockUpdateEvent buildFromInvoiceEvent(InvoiceEvent invoiceEvent) {
        log.info("Building invoice");
        List<StockUpdateEvent.Info> infos = new ArrayList<>();

        List<InvoiceEvent.InvoiceMenuItem> items = invoiceEvent.getItems();

        for (var item : items) {

            List<MenuIngredientResponse> data =
                    menuClient.getMenuIngredientsById(item.getMenuId())
                            .getBody()
                            .getData();

            double orderQuantity = item.getQuantity();

            for (var detail : data) {

                double deductionQuantity = detail.getQuantity() * orderQuantity;

                infos.add(
                        new StockUpdateEvent.Info(
                                detail.getVariantId(),
                                detail.getUnitId(),
                                BigDecimal.valueOf(deductionQuantity),
                                StockUpdateSource.SALE
                        )
                );
            }
        }
        log.info("Builded invoice");

        return new StockUpdateEvent(null, invoiceEvent.getInvoiceId(), infos);

    }
}
