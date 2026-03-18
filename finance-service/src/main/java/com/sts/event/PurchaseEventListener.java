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

    private final PurchaseRecordRepository purchaseRecordRepository;
    private final PurchaseRecordMapper purchaseRecordMapper;
    private final KafkaProperties kafkaProperties;

    @KafkaListener(topics = "#{@kafkaProperties.getTopic('purchase-event')}",
            containerFactory = "purchaseKafkaListenerContainerFactory")
    public void listen(PurchaseCreatedEvent event, Acknowledgment acknowledgment) {

        log.info(AppConstants.LOG_MESSAGES.PURCHASE_EVENT_RECEIVED, event.getPurchaseId());

        try {
            PurchaseRecord purchaseRecord = purchaseRecordMapper.buildPurchaseRecord(event);

            purchaseRecordRepository.save(purchaseRecord);

            acknowledgment.acknowledge();

            log.info(AppConstants.LOG_MESSAGES.PURCHASE_RECORD_SAVED, event.getPurchaseId());

        } catch (Exception e) {
            log.error(AppConstants.LOG_MESSAGES.PURCHASE_EVENT_FAILED, event.getPurchaseId(), e);
            throw e;
        }
    }


    @KafkaListener(topics = "PURCHASE_EVENT", groupId = "debug-group",
            containerFactory = "purchaseKafkaListenerContainerFactory"
    )
    public void debug(String msg) {
        System.out.println("MSG: " + msg);
    }

}