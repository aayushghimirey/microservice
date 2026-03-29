package com.sts.event;

import com.sts.mapper.StockSnapshotMapper;
import com.sts.model.StockSnapshot;
import com.sts.repository.StockSnapshotRepository;
import com.sts.topics.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockListener {

    private final StockSnapshotRepository stockSnapshotRepository;
    private final StockSnapshotMapper stockSnapshotMapper;
    private final KafkaProperties kafkaProperties;


    @KafkaListener(topics = "#{@kafkaProperties.getTopic('stock-event')}")
    public void onStockEvent(StockEvent event, Acknowledgment acknowledgment) {
        log.info("Received StockEvent for stockId: {}", event.stockId());

        if (stockSnapshotRepository.existsByStockId(event.stockId())) {
            log.warn("StockSnapshot already exists for stockId: {}. Skipping creation.", event.stockId());
            throw new IllegalStateException("StockSnapshot already exists for stockId: " + event.stockId());
        }

        StockSnapshot stockSnapshot = stockSnapshotMapper.toStockSnapshot(event);

        stockSnapshotRepository.save(stockSnapshot);

        log.info("Saved StockSnapshot for stockId: {}", event.stockId());

        acknowledgment.acknowledge();


    }

}
