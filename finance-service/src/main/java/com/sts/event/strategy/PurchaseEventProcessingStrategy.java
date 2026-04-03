package com.sts.event.strategy;

import com.sts.event.EventProcessingStrategy;
import com.sts.event.PurchaseCreatedEvent;
import com.sts.mapper.PurchaseRecordMapper;
import com.sts.repository.PurchaseRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Slf4j
@Component
public class PurchaseEventProcessingStrategy implements EventProcessingStrategy<PurchaseCreatedEvent> {

    private final PurchaseRecordRepository purchaseRecordRepository;
    private final PurchaseRecordMapper purchaseRecordMapper;

    @Override
    public void process(PurchaseCreatedEvent event) {

        if (purchaseRecordRepository.findByPurchaseId(event.getPurchaseId()).isPresent()) {
            log.warn("Duplicate purchase id found - {}", event.getPurchaseId());
        }

        purchaseRecordRepository.save(purchaseRecordMapper.buildRecord(event));
    }
}
