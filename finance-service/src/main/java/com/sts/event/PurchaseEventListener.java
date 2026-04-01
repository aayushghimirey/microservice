package com.sts.event;


import com.sts.mapper.PurchaseRecordMapper;

import com.sts.topics.KafkaProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.sts.model.PurchaseRecord;
import com.sts.repository.PurchaseRecordRepository;
import com.sts.utils.constant.AppConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PurchaseEventListener {

    private final KafkaProperties kafkaProperties;

    private final PurchaseRecordRepository purchaseRecordRepository;

    private final PurchaseRecordMapper purchaseRecordMapper;

    @KafkaListener(topics = "#{@kafkaProperties.getTopic('purchase-event')}",
            containerFactory = "purchaseKafkaListenerContainerFactory")
    public void listen(PurchaseCreatedEvent event, Acknowledgment acknowledgment) {

        log.info("Purchase event received - purchase: {}", event.getPurchaseId());

        try {
            PurchaseRecord purchaseRecord = purchaseRecordMapper.buildPurchaseRecord(event);

            purchaseRecordRepository.save(purchaseRecord);

            acknowledgment.acknowledge();

            log.info("Purchase record saved - invoiceId: {}", event.getPurchaseId());

        } catch (Exception e) {
            log.error("Purchase event processing failed - invoiceId: {}", event.getPurchaseId(), e);
        }
    }

}