package com.sts.shared;

import com.sts.entity.OutboxEventType;
import com.sts.enums.AggregateType;
import com.sts.event.StockEvent;
import com.sts.helper.outbox.OutboxPublisher;
import com.sts.mapper.StockMapper;
import com.sts.model.stock.Stock;
import com.sts.topics.KafkaProperties;
import com.sts.utils.constant.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockOutboxPublisher {

    private final StockMapper stockMapper;
    private final OutboxPublisher outboxPublisher;
    private final KafkaProperties kafkaProperties;


    /*
     * This method is used to publish outbox events for stock changes or creation.
     * this is used to save snapshot of stock for menu service
     * */
    public void publish(Stock stock, OutboxEventType eventType) {
        StockEvent event = stockMapper.toStockEvent(stock);
        outboxPublisher.publish(
                AggregateType.STOCK,
                stock.getId(),
                eventType,
                event,
                kafkaProperties.getTopic(AppConstants.KAFKA_TOPIC_STOCK_EVENT));
    }
}