package com.sts.application.event;

import com.sts.model.PurchaseRecord;
import com.sts.repository.PurchaseRecordRepository;
import com.sts.event.PurchaseCreatedEvent;
import com.sts.topics.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PurchaseEventListener {

    private final PurchaseRecordRepository purchaseRecordRepository;
    private final KafkaProperties kafkaProperties;

    @KafkaListener(
            topics = "#{@kafkaProperties.getTopic('purchase-event')}",
            groupId = "#{@kafkaProperties.getGroup('finance-group')}",
            containerFactory = "financeKafkaListenerContainerFactory"
    )
    public void listen(
            ConsumerRecord<String, PurchaseCreatedEvent> record,
            Acknowledgment acknowledgment
    ) {
        log.info("Message received from topic {}, offset {}",
                record.topic(), record.offset());

        try {
            PurchaseCreatedEvent value = record.value();

            PurchaseRecord purchaseRecord = new PurchaseRecord();
            purchaseRecord.setPurchaseId(value.getPurchaseId());
            purchaseRecord.setBillingType(value.getBillingType());
            purchaseRecord.setMoneyTransaction(value.getMoneyTransaction());
            purchaseRecord.setVatAmount(
                    value.getVatAmount() != null ? value.getVatAmount() : BigDecimal.ZERO
            );
            purchaseRecord.setGrossTotal(value.getGrossTotal());

            purchaseRecordRepository.save(purchaseRecord);
            acknowledgment.acknowledge();

            log.info("Purchase record saved for purchaseId {}", value.getPurchaseId());

        } catch (Exception e) {
            log.error("Failed at offset {}", record.offset(), e);
        }
    }
}