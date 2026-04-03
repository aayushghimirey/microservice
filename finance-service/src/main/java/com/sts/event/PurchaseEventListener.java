package com.sts.event;


import com.sts.event.strategy.PurchaseEventProcessingStrategy;
import com.sts.topics.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PurchaseEventListener {

    private final KafkaProperties kafkaProperties;
    private final PurchaseEventProcessingStrategy purchaseEventProcessingStrategy;

    @KafkaListener(topics = "#{@kafkaProperties.getTopic('purchase-event')}",
            containerFactory = "purchaseKafkaListenerContainerFactory")
    @Transactional
    public void handlePurchaseEvent(PurchaseCreatedEvent event, Acknowledgment acknowledgment) {

        log.info("Purchase event received - purchase: {}", event.getPurchaseId());

        try {
            purchaseEventProcessingStrategy.process(event);
            acknowledgment.acknowledge();
            log.info("Purchase record saved - invoiceId: {}", event.getPurchaseId());
        } catch (Exception e) {
            log.error("Purchase event processing failed - invoiceId: {}", event.getPurchaseId(), e);
        }
    }

}