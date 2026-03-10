package com.sts.event.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.sts.event.InvoiceEvent;
import com.sts.event.InvoiceStockUpdateResolver;
import com.sts.support.event.DomainEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceEventListener {

    private final InvoiceStockUpdateResolver resolver;
    private final DomainEventPublisher domainEventPublisher;

    @KafkaListener(topics = "#{@kafkaProperties.getTopic('invoice-event')}", containerFactory = "invoiceKafkaListenerContainerFactory")
    public void listen(InvoiceEvent event, Acknowledgment acknowledgment) {
        log.info("Invoice event received invoiceId={}", event.getInvoiceId());

        try {
            var stockUpdateEvent = resolver.resolve(event);
            domainEventPublisher.publish(stockUpdateEvent);

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process invoice event invoiceId={}", event.getInvoiceId(), e);
            throw e;
        }
    }
}