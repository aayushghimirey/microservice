package com.sts.helper.outbox;


import com.sts.entity.OutboxEvent;
import com.sts.enums.AggregateType;
import com.sts.exception.OutboxPublishException;
import com.sts.mapper.OutboxMapper;
import com.sts.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final ObjectMapper objectMapper;
    private final OutboxMapper outboxMapper;
    private final OutboxEventRepository outboxEventRepository;

    /**
     * Serialize event
     */
    public String serialize(Object event) {

        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new OutboxPublishException("Exception occurs while processing json");
        }
    }

    /**
     * Build Outbox Event
     */
    public void publish(
            AggregateType aggregateType,
            UUID aggregateId,
            com.sts.entity.OutboxEventType eventType,
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

        log.info("Saved outbox event for topic: {}", outboxEvent.getTopic());
    }

}
