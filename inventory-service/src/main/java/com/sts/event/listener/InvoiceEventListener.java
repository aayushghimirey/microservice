package com.sts.event.listener;

import com.sts.event.factory.StockUpdateFactoryRegistry;
import com.sts.utils.constant.AppConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.sts.event.InvoiceEvent;
import com.sts.helper.event.DomainEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


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

    @KafkaListener(topics = "#{@kafkaProperties.getTopic('invoice-event')}", containerFactory = "invoiceKafkaListenerContainerFactory")
    public void listen(InvoiceEvent event, Acknowledgment acknowledgment) {
        log.info(AppConstants.Logs.INVOICE_EVENT_RECEIVED, event.getInvoiceId());

        try {

            domainEventPublisher.publish(stockUpdateFactoryRegistry.forInvoice(event));

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error(AppConstants.Logs.FAILED_TO_PROCESS_INVOICE, event.getInvoiceId(), e);
            throw e;
        }
    }
}