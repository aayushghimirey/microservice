package com.sts.publisher;


import com.sts.entity.OutboxEvent;
import com.sts.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelayString = "${outbox.poller.delay-ms:5000}")
    public void publishPendingEvents() {
        List<OutboxEvent> events = outboxEventRepository.findByProcessedFalse();

        for (OutboxEvent event : events) {
            String topic = event.getTopic() != null ? event.getTopic() : "shared-events";

            try {
                // convert JSON string payload to Object
                Object payloadObj = objectMapper.readValue(event.getPayload(), Object.class);

                kafkaTemplate.send(topic, payloadObj);

                event.setProcessed(true);
                outboxEventRepository.save(event);

                log.info("Event publish on topic {}", topic);


            } catch (Exception e) {
                // handle deserialization error (log or retry later)
                e.printStackTrace();
            }
        }


    }
}
