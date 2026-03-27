package com.sts.shared;

import com.sts.entity.OutboxEventType;
import com.sts.enums.AggregateType;
import com.sts.event.PurchaseCreatedEvent;
import com.sts.helper.outbox.OutboxPublisher;
import com.sts.mapper.PurchaseMapper;
import com.sts.model.purchase.Purchase;
 import com.sts.topics.KafkaProperties;
import com.sts.utils.constant.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PurchaseOutboxPublisher {

    private final PurchaseMapper purchaseMapper;
    private final OutboxPublisher outboxPublisher;
    private final KafkaProperties kafkaProperties;

    public void publish(Purchase purchase) {
        PurchaseCreatedEvent event = purchaseMapper.toPurchaseEvent(purchase);

        outboxPublisher.publish(
                AggregateType.PURCHASE,
                purchase.getId(),
                OutboxEventType.CREATED,
                event,
                kafkaProperties.getTopic(AppConstants.KAFKA_TOPIC_PURCHASE_EVENT));
    }

}
