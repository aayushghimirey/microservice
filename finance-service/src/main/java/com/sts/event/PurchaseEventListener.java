package com.sts.event;

import java.math.BigDecimal;

import org.apache.kafka.clients.consumer.ConsumerRecord;
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

    @KafkaListener(topics = "#{@kafkaProperties.getTopic('purchase-event')}",
            containerFactory = "purchaseKafkaListenerContainerFactory")
    public void listen(PurchaseCreatedEvent event, Acknowledgment acknowledgment) {

        log.info(AppConstants.LOG_MESSAGES.PURCHASE_EVENT_RECEIVED, event.getPurchaseId());

        try {
            PurchaseRecord purchaseRecord = buildPurchaseRecord(event);

            purchaseRecordRepository.save(purchaseRecord);

            acknowledgment.acknowledge();

            log.info(AppConstants.LOG_MESSAGES.PURCHASE_RECORD_SAVED, event.getPurchaseId());

        } catch (Exception e) {
            log.error(AppConstants.LOG_MESSAGES.PURCHASE_EVENT_FAILED, event.getPurchaseId(), e);
            throw e;
        }
    }

    // -- private helper
    public PurchaseRecord buildPurchaseRecord(PurchaseCreatedEvent event) {
        return PurchaseRecord.builder()
                .purchaseId(event.getPurchaseId())
                .billingType(event.getBillingType())
                .moneyTransaction(event.getMoneyTransaction())
                .vatAmount(event.getVatAmount() != null ? event.getVatAmount() : BigDecimal.ZERO)
                .grossTotal(event.getGrossTotal())
                .build();
    }

}