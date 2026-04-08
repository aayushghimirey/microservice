package com.sts.mapper;

import com.sts.entity.OutboxEvent;
import com.sts.entity.OutboxEventType;
import com.sts.enums.AggregateType;
import com.sts.filter.TenantHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class OutboxMapper {

    public OutboxEvent map(AggregateType aggregateType, UUID aggregateId, OutboxEventType eventType, String payload, String topic) {
        return OutboxEvent.builder()
                .aggregateType(aggregateType)
                .aggregateId(aggregateId)
                .topic(topic)
                .eventType(eventType)
                .payload(payload)
                .processed(false)
                .createdAt(LocalDateTime.now())
                .tenantId(TenantHolder.getTenantId())
                .build();
    }
}