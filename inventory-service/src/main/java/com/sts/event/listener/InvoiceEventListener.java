package com.sts.event.listener;

import com.sts.event.factory.StockUpdateFactoryRegistry;
import com.sts.topics.KafkaProperties;
import com.sts.utils.constant.AppConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.sts.event.InvoiceEvent;
import com.sts.helper.event.DomainEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


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
    private final KafkaProperties kafkaProperties;

    @KafkaListener(topics = "INVOICE_EVENT", containerFactory = "invoiceKafkaListenerContainerFactory")
    @Transactional
    public void listen(InvoiceEvent event, Acknowledgment acknowledgment) {
        log.info(AppConstants.Logs.INVOICE_EVENT_RECEIVED, event.getInvoiceId());
        log.info("Received invoice event");
        try {

            domainEventPublisher.publish(stockUpdateFactoryRegistry.forInvoice(event));


        } catch (Exception e) {

            log.error(AppConstants.Logs.FAILED_TO_PROCESS_INVOICE, event.getInvoiceId(), e);
            throw e;
        } finally {
            acknowledgment.acknowledge();
        }
    }
}