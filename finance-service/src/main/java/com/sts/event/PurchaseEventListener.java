package com.sts.event;


import com.sts.event.strategy.PurchaseEventProcessingStrategy;
import com.sts.filter.TenantHolder;
import com.sts.topics.KafkaProperties;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PurchaseEventListener {

    private final KafkaProperties kafkaProperties;
    private final PurchaseEventProcessingStrategy purchaseEventProcessingStrategy;
    private final RlsContext rlsContext;

    @KafkaListener(topics = "#{@kafkaProperties.getTopic('purchase-event')}",
            containerFactory = "purchaseKafkaListenerContainerFactory")
    public void handlePurchaseEvent(PurchaseCreatedEvent event, Acknowledgment acknowledgment) {


        rlsContext.with("app.tenant_id", event.getTenantId()).apply();
        TenantHolder.setTenantId(event.getTenantId());


        log.info("Purchase event received - purchaseId: {}", event.getPurchaseId());

        purchaseEventProcessingStrategy.process(event);

        acknowledgment.acknowledge();

        log.info("Purchase record saved - purchaseId: {}", event.getPurchaseId());

        TenantHolder.clear();


    }

}