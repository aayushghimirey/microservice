package com.sts.publisher;

import com.sts.entity.OutboxEvent;
import com.sts.filter.TenantHolder;
import com.sts.repository.OutboxEventRepository;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxKafkaPublisher {

    @Qualifier("outboxKafkaTemplate")
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxEventRepository outboxEventRepository;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void publishKafka() {


        List<OutboxEvent> events = outboxEventRepository.findByProcessedFalse();

        log.info("Publishing {} events to Kafka", events.size());

        for (OutboxEvent event : events) {

            kafkaTemplate.send(event.getTopic(), event.getPayload())
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            try {
                                event.setProcessed(true);
                                outboxEventRepository.save(event);
                            } catch (Exception dbEx) {
                                log.error("DB update failed after Kafka send for event {}", event.getId(), dbEx);
                            }
                        } else {
                            log.error("Kafka publish failed for event {}", event.getId(), ex);
                        }
                    });
        }
    }

}