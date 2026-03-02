package com.sts.application.event;

import com.sts.domain.model.PurchaseRecord;
import com.sts.domain.repository.PurchaseRecordRepository;
import com.sts.event.PurchaseCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PurchaseEventListener {

    private static final Logger log = LoggerFactory.getLogger(PurchaseEventListener.class);

    @Value("${spring.kafka.groups.finance-group}")
    private String finance_group;


    private final PurchaseRecordRepository purchaseRecordRepository;


    @KafkaListener(groupId = "${spring.kafka.groups.finance-group}", topics = "${spring.kafka.topics.purchase-event}", containerFactory = "financeKafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, PurchaseCreatedEvent> record, Acknowledgment acknowledgment) {
        log.info("Message received form topic {}, on offset {}", record.topic(), record.offset());

        try {
            PurchaseCreatedEvent value = record.value();

            PurchaseRecord purchaseRecord = new PurchaseRecord();
            purchaseRecord.setPurchaseId(value.getPurchaseId());
            purchaseRecord.setBillingType(value.getBillingType());
            purchaseRecord.setMoneyTransaction(value.getMoneyTransaction());
            purchaseRecord.setVatAmount(value.getVatAmount() != null ? value.getVatAmount() : BigDecimal.ZERO);
            purchaseRecord.setGrossTotal(value.getGrossTotal());

            purchaseRecordRepository.save(purchaseRecord);
            acknowledgment.acknowledge();

            log.info("Purchase record saved for purchaseId {}", value.getPurchaseId());


        } catch (Exception e) {
            log.error("Failed to process message at offset {}: {}", record.offset(), e.getMessage());
            log.error("Error", e);
        }

    }
}
