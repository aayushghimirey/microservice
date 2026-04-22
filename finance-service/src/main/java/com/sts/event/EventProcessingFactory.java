//package com.sts.event;
//
//import com.sts.event.strategy.InvoiceEventProcessingStrategy;
//import com.sts.event.strategy.PurchaseEventProcessingStrategy;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class EventProcessingFactory {
//
//    private final InvoiceEventProcessingStrategy invoiceEventProcessingStrategy;
//    private final PurchaseEventProcessingStrategy purchaseEventProcessingStrategy;
//
//
//    public <T> EventProcessingStrategy<T> getEventProcessingStrategy(T type) {
//        if (type.getClass().equals(InvoiceEvent.class)) {
//            return invoiceEventProcessingStrategy;
//        } else if (type.getClass().equals(PurchaseCreatedEvent.class)) {
//            return purchaseEventProcessingStrategy;
//        }
//
//        return null;
//    }
//
//}
