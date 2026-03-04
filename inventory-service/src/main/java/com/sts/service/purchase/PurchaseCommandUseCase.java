package com.sts.purchase.application.usecase;


import com.sts.entity.OutboxEvent;
import com.sts.entity.OutboxEventType;
import com.sts.event.PurchaseCreatedEvent;
import com.sts.mapper.OutboxMapper;
import com.sts.purchase.command.CreatePurchaseCommand;
import com.sts.purchase.command.PurchaseCommandHandler;
import com.sts.repository.OutboxEventRepository;
import com.sts.model.purchase.Purchase;
import com.sts.repository.PurchaseRepository;
import com.sts.stock.application.event.StockUpdateEventBuilder;
import com.sts.repository.StockVariantRepository;
import com.sts.topics.KafkaTopics;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
@Slf4j
public class PurchaseCommandUseCase {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseCommandHandler purchaseCommandHandler;
    private final OutboxMapper outboxMapper;
    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTopics topics;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher publisher;
    private final StockVariantRepository stockVariantRepository;
    private final StockUpdateEventBuilder stockUpdateEventBuilder;

    @Transactional
    public Purchase createPurchase(CreatePurchaseCommand command) {
        try {
            Purchase purchase = purchaseCommandHandler.buildPurchase(command);
            purchaseRepository.save(purchase);

            // publish to kafka
            publishPurchaseCreatedEvent(purchase);

            // publish to application
            publisher.publishEvent(stockUpdateEventBuilder.buildStockUpdateEventFromPurchase(purchase));

            return purchase;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create purchase", e);
        }
    }


    // ------------- HELPER / BUILDER ----------------

    private void publishPurchaseCreatedEvent(Purchase purchase) throws Exception {
        PurchaseCreatedEvent event = new PurchaseCreatedEvent(
                purchase.getId(),
                purchase.getBillingType(),
                purchase.getMoneyTransaction(),
                purchase.getVatAmount(),
                purchase.getGrossTotal()
        );

        String payload = objectMapper.writeValueAsString(event);

        OutboxEvent outboxEvent = outboxMapper.map(
                "PURCHASE",
                purchase.getId().toString(),
                OutboxEventType.CREATED,
                payload,
                topics.getPurchaseEvent()
        );

        outboxEventRepository.save(outboxEvent);
    }


}

