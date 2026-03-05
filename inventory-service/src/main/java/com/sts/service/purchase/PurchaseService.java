package com.sts.service.purchase;


import com.sts.dto.request.CreatePurchaseCommand;
import com.sts.dto.response.PurchaseResponse;
import com.sts.entity.OutboxEvent;
import com.sts.entity.OutboxEventType;
import com.sts.event.PurchaseCreatedEvent;
import com.sts.event.StockUpdateEventBuilder;
import com.sts.exception.DuplicatePurchase;
import com.sts.exception.InvalidUnitType;
import com.sts.exception.UnitNotFound;
import com.sts.exception.VariantNotFound;
import com.sts.mapper.OutboxMapper;
import com.sts.mapper.PurchaseMapper;
import com.sts.model.purchase.Purchase;
import com.sts.model.stock.VariantUnit;
import com.sts.repository.OutboxEventRepository;
import com.sts.repository.PurchaseRepository;
import com.sts.repository.StockVariantRepository;
import com.sts.repository.VariantUnitRepository;
import com.sts.topics.KafkaProperties;
import com.sts.utils.contant.AppConstants;
import com.sts.utils.enums.UnitType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
@Slf4j
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final OutboxMapper outboxMapper;
    private final OutboxEventRepository outboxEventRepository;
    private final KafkaProperties kafkaProperties;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher publisher;
    private final StockUpdateEventBuilder stockUpdateEventBuilder;

    private final StockVariantRepository stockVariantRepository;
    private final VariantUnitRepository variantUnitRepository;

    @Transactional
    public PurchaseResponse createPurchase(CreatePurchaseCommand command) {


        if (purchaseRepository.existsByInvoiceNumber(command.invoiceNumber())) {
            throw new DuplicatePurchase(String.format(AppConstants.INVOICE_NUMBER_EXITS, command.invoiceNumber()));
        }
        for (var item : command.items()) {
            if (!stockVariantRepository.existsById(item.variantId())) {
                throw new VariantNotFound(String.format(AppConstants.VARIANT_NOT_FOUND, item.variantId()));
            }
            VariantUnit variantUnit = variantUnitRepository.findById(item.unitId()).orElseThrow(
                    () -> new UnitNotFound(String.format(AppConstants.UNIT_NOT_FOUND, item.unitId()))
            );
            if (variantUnit.getUnitType().equals(UnitType.SELL)) {
                throw new InvalidUnitType(String.format(AppConstants.INVALID_UNIT_TYPE, variantUnit.getUnitType()));
            }
        }

        Purchase purchase = purchaseMapper.buildPurchase(command);
        purchaseRepository.save(purchase);

        // publish to kafka
        publishPurchaseCreatedEvent(purchase);

        // publish to application
        publisher.publishEvent(stockUpdateEventBuilder.buildStockUpdateEventFromPurchase(purchase));

        return purchaseMapper.toResponse(purchase);

    }


    // ------------- HELPER / BUILDER ----------------

    private void publishPurchaseCreatedEvent(Purchase purchase) {
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
                kafkaProperties.getTopic("purchase-event")
        );

        outboxEventRepository.save(outboxEvent);
    }


}

