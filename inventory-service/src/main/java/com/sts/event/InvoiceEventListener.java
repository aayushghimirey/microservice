package com.sts.event;

import com.sts.topics.KafkaProperties;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InvoiceEventListener {

    private static final Logger log = LoggerFactory.getLogger(InvoiceEventListener.class);
    private final KafkaProperties kafkaProperties;
    private final StockUpdateEventBuilder stockUpdateEventBuilder;
    private final ApplicationEventPublisher applicationEventPublisher;

    @KafkaListener(
            topics = "#{@kafkaProperties.getTopic('invoice-event')}",
            containerFactory = "invoiceKafkaListenerContainerFactory"
    )
    public void listen(InvoiceEvent event, Acknowledgment acknowledgment) {
        log.info("Event received with id {}", event.getInvoiceId());

        StockUpdateEvent stockUpdateEvent = stockUpdateEventBuilder.buildFromInvoiceEvent(event);
        applicationEventPublisher.publishEvent(stockUpdateEvent);
    }
}
