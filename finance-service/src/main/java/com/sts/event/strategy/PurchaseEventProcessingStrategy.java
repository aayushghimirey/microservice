package com.sts.event.strategy;

import com.sts.event.PurchaseCreatedEvent;
import com.sts.filter.TenantHolder;
import com.sts.mapper.PurchaseRecordMapper;
import com.sts.repository.PurchaseRecordRepository;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Slf4j
@Component
public class PurchaseEventProcessingStrategy extends AbstractEventProcessingStrategy<PurchaseCreatedEvent> {

    private final PurchaseRecordRepository purchaseRecordRepository;
    private final PurchaseRecordMapper purchaseRecordMapper;
    private final RlsContext rlsContext;


    @Override
    @Transactional
    protected void save(PurchaseCreatedEvent event) {

        TenantHolder.setTenantId(event.getTenantId());

        rlsContext.with("app.tenant_id", event.getTenantId()).apply();

        if (purchaseRecordRepository.findByPurchaseId(event.getPurchaseId()).isPresent()) {
            log.warn("Purchase record exits - purchaseId: {}", event.getPurchaseId());
            return;
        }

        purchaseRecordRepository.save(purchaseRecordMapper.buildEntity(event));
    }


}
