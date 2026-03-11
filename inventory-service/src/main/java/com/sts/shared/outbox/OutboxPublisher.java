package com.sts.shared.outbox;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.sts.utils.constant.AppConstants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sts.entity.OutboxEvent;
import com.sts.entity.OutboxEventType;
import com.sts.enums.AggregateType;
import com.sts.exception.OutboxPublishException;
import com.sts.mapper.OutboxMapper;
import com.sts.repository.OutboxEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final ObjectMapper objectMapper;
    private final OutboxMapper outboxMapper;
    private final OutboxEventRepository outboxEventRepository;

    /**
     * Serialize event to JSON
     */
    public String serialize(Object event) {

        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error(AppConstants.ERROR_MESSAGES.OUTBOX_SERIALIZATION_FAILED, e);
            throw new OutboxPublishException(AppConstants.ERROR_MESSAGES.OUTBOX_PUBLISH_FAILED);
        }
    }

    /**
     * Build Outbox Event
     */
    public void publish(
            AggregateType aggregateType,
            UUID aggregateId,
            OutboxEventType eventType,
            Object eventPayload,
            String topic) {

        String payload = serialize(eventPayload);

        OutboxEvent outboxEvent = outboxMapper.map(
                aggregateType,
                aggregateId,
                eventType,
                payload,
                topic);

        outboxEventRepository.save(outboxEvent);
    }

}
