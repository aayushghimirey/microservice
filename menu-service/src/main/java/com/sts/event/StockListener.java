package com.sts.event;

import com.sts.mapper.StockSnapshotMapper;
import com.sts.repository.StockSnapshotRepository;
import com.sts.topics.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockListener {

    private static final Logger log = LoggerFactory.getLogger(StockListener.class);
    private final StockSnapshotRepository stockSnapshotRepository;
    private final StockSnapshotMapper stockSnapshotMapper;
    private final KafkaProperties kafkaProperties;


    @KafkaListener(topics = "#{@kafkaProperties.getTopic('stock-event')}")
    public void onStockEvent(StockEvent event, Acknowledgment acknowledgment) {
        log.info("Received StockEvent for stockId: {}", event.id());

        stockSnapshotRepository.findByStockId(event.id())
                .ifPresentOrElse(
                        existingSnapshot -> {
                            log.info("Updating existing StockSnapshot for stockId: {}", event.id());
//                            var updatedSnapshot = stockSnapshotMapper.updateStockSnapshotFromEvent(existingSnapshot, event);
//                            stockSnapshotRepository.save(updatedSnapshot);
                        },
                        () -> {
                            log.info("Creating new StockSnapshot for stockId: {}", event.id());
                            var snapshot = stockSnapshotMapper.buildStockSnapshotFromEvent(event);
                            stockSnapshotRepository.save(snapshot);
                        }
                );


    }

}
