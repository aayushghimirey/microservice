package com.sts.event;

import com.sts.event.strategy.InvoiceEventProcessingStrategy;
import com.sts.topics.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceEventListener {


    private final KafkaProperties kafkaProperties;
    private final InvoiceEventProcessingStrategy invoiceEventProcessingStrategy;

    @KafkaListener(topics = "#{@kafkaProperties.getTopic('invoice-event')}",
            containerFactory = "invoiceKafkaListenerContainerFactory")
    public void handleInvoiceEvent(InvoiceEvent event, Acknowledgment acknowledgment) {

        log.info("Invoice event received - invoiceId: {}", event.getInvoiceId());

        invoiceEventProcessingStrategy.process(event);

        acknowledgment.acknowledge();

        log.info("Invoice record saved - invoiceId: {}", event.getInvoiceId());

    }


}
