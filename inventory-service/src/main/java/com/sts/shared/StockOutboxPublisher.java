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
     * This is used to publish and listen by menu service
     * to save the stock information.
     * */
    public void publish(Stock stock) {
        StockEvent event = stockMapper.toStockEvent(stock);
        outboxPublisher.publish(
                AggregateType.STOCK,
                stock.getId(),
                OutboxEventType.CREATED,
                event,
                kafkaProperties.getTopic(AppConstants.KAFKA_TOPIC_STOCK_EVENT));
    }
}