package com.sts.shared.outbox;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.sts.entity.OutboxEvent;
import com.sts.entity.OutboxEventType;
import com.sts.enums.AggregateType;
import com.sts.exception.OutboxPublishException;
import com.sts.mapper.OutboxMapper;
import com.sts.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final ObjectMapper objectMapper;
    private final OutboxMapper outboxMapper;
    private final OutboxEventRepository repository;

    public void publish(AggregateType aggregateType, UUID aggregateId, OutboxEventType eventType, Object payloadObject, String topic) {
        try {
            String payload = objectMapper.writeValueAsString(payloadObject);

            OutboxEvent event = outboxMapper.map(aggregateType, aggregateId, eventType, payload, topic);

            repository.save(event);
        } catch (JsonProcessingException e) {
            throw new OutboxPublishException("Failed to serialize outbox payload", e);
        }
    }
}