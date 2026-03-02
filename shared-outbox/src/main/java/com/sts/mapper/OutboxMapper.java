package com.sts.mapper;

import com.sts.entity.OutboxEvent;
import com.sts.entity.OutboxEventType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OutboxMapper {

    public OutboxEvent map(String aggregateType, String aggregateId, OutboxEventType eventType, String payload, String topic) {
        return OutboxEvent.builder()
                .aggregateType(aggregateType)
                .aggregateId(aggregateId)
                .topic(topic)
                .eventType(eventType)
                .payload(payload)
                .processed(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
}