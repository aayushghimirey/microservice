package com.sts.event.strategy;

import com.sts.event.PurchaseCreatedEvent;
import com.sts.mapper.PurchaseRecordMapper;
import com.sts.repository.PurchaseRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Slf4j
@Component
public class PurchaseEventProcessingStrategy extends AbstractEventProcessingStrategy<PurchaseCreatedEvent> {

    private final PurchaseRecordRepository purchaseRecordRepository;
    private final PurchaseRecordMapper purchaseRecordMapper;


    @Override
    protected void save(PurchaseCreatedEvent event) {
        if (purchaseRecordRepository.findByPurchaseId(event.getPurchaseId()).isPresent()) {
            log.warn("Purchase record exits - purchaseId: {}", event.getPurchaseId());
            return;
        }

        purchaseRecordRepository.save(purchaseRecordMapper.buildEntity(event));
    }


}
