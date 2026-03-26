//package com.sts.event.factory;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.UUID;
//
//import org.springframework.stereotype.Component;
//
//import com.sts.event.StockUpdateEvent;
//import com.sts.model.purchase.Purchase;
//import com.sts.utils.enums.StockUpdateSource;
//
//@Component
//public class StockUpdateEventFactory {
//
//    public StockUpdateEvent buildFromPurchase(Purchase purchase) {
//        var infos = purchase.getPurchaseItems().stream()
//                .map(item -> new StockUpdateEvent.StockUpdateItem(
//                        item.getVariantId(),
//                        item.getUnitId(),
//                        item.getQuantity(),
//                        StockUpdateSource.PURCHASE))
//                .toList();
//
//        return new StockUpdateEvent(purchase.getId(), null, infos);
//    }
//
//    public StockUpdateEvent buildFromAdjustment(UUID variantId, UUID unitId, BigDecimal quantity) {
//        return new StockUpdateEvent(
//                null,
//                null,
//                List.of(new StockUpdateEvent.StockUpdateItem(
//                        variantId,
//                        unitId,
//                        quantity,
//                        StockUpdateSource.ADJUSTMENT)));
//    }
//
//    public StockUpdateEvent buildFromInvoiceItems(UUID invoiceId, List<ResolvedInvoiceItem> resolvedItems) {
//        var items = resolvedItems.stream()
//                .map(item -> new StockUpdateEvent.StockUpdateItem(
//                        item.variantId(),
//                        item.unitId(),
//                        item.quantity(),
//                        StockUpdateSource.SALE))
//                .toList();
//
//        return new StockUpdateEvent(null, invoiceId, items);
//    }
//
//    public record ResolvedInvoiceItem(
//            UUID variantId,
//            UUID unitId,
//            BigDecimal quantity) {
//    }
//}
