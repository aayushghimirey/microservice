package com.sts.event.listener;

import com.sts.event.InvoiceEvent;
import com.sts.event.factory.StockUpdateFactoryRegistry;
import com.sts.helper.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


/*
 * Work flow:
 * Listen from kafka, create StockUpdateEvent
 * Purchase Application event
 * Stock Update Listener receives and process StockUpdateProcessor
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceEventListener {

    private final DomainEventPublisher domainEventPublisher;
    private final StockUpdateFactoryRegistry stockUpdateFactoryRegistry;

    @KafkaListener(
            topics = "${app.kafka.topics.invoice-event}",
            containerFactory = "invoiceKafkaListenerContainerFactory"
    )
    public void listen(InvoiceEvent event, Acknowledgment acknowledgment) {

        log.info("Kafka event received: invoiceId={} eventType={}",
                event.getInvoiceId(),
                event.getClass().getSimpleName());

        try {
            log.info("Processing invoice event: invoiceId={}", event.getInvoiceId());

            // publish domains stock update event
            domainEventPublisher.publish(stockUpdateFactoryRegistry.forInvoice(event));

            log.info("Successfully processed invoice event: invoiceId={}", event.getInvoiceId());

            acknowledgment.acknowledge();

        } catch (Exception e) {
            log.error("Failed to process invoice event: invoiceId={} error={}", event.getInvoiceId(), e.getMessage(), e);
            throw e;
        }
    }
}