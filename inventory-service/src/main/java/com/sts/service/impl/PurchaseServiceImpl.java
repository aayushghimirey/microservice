package com.sts.service.impl;

import com.sts.dto.request.CreatePurchaseCommand;
import com.sts.dto.response.PurchaseResponse;
import com.sts.entity.OutboxEvent;
import com.sts.entity.OutboxEventType;
import com.sts.event.PurchaseCreatedEvent;
import com.sts.event.StockUpdateEventBuilder;
import com.sts.exception.BusinessValidationException;
import com.sts.exception.DuplicateResourceException;
import com.sts.exception.ResourceNotFoundException;
import com.sts.mapper.OutboxMapper;
import com.sts.mapper.PurchaseMapper;
import com.sts.model.purchase.Purchase;
import com.sts.model.stock.VariantUnit;
import com.sts.repository.OutboxEventRepository;
import com.sts.repository.PurchaseRepository;
import com.sts.repository.StockVariantRepository;
import com.sts.repository.VariantUnitRepository;
import com.sts.service.PurchaseService;
import com.sts.topics.KafkaProperties;
import com.sts.utils.contant.AppConstants;
import com.sts.utils.enums.UnitType;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;


@Service
@AllArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {


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

    @Override
    @Transactional
    public PurchaseResponse createPurchase(CreatePurchaseCommand command) {

        if (purchaseRepository.existsByInvoiceNumber(command.invoiceNumber())) {
            throw new DuplicateResourceException(
                    String.format(AppConstants.ERROR_MESSAGES.INVOICE_NUMBER_EXISTS, command.invoiceNumber()));
        }


        validateItems(command);

        Purchase purchase = purchaseMapper.buildPurchase(command);
        purchaseRepository.save(purchase);

        createPurchaseOutboxEvent(purchase);


        // publish to application
        publisher.publishEvent(stockUpdateEventBuilder.buildFromPurchase(purchase));


        return purchaseMapper.toResponse(purchase);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseResponse> getAllPurchases(Pageable pageable) {
        return purchaseRepository.findAll(pageable).map(purchaseMapper::toResponse);
    }

    // ------------- HELPER / BUILDER ----------------

    private void validateItems(CreatePurchaseCommand command) {
        for (var item : command.items()) {

            if (!stockVariantRepository.existsById(item.variantId())) {
                throw new ResourceNotFoundException(
                        String.format(AppConstants.ERROR_MESSAGES.VARIANT_NOT_FOUND, item.variantId()));
            }

            VariantUnit variantUnit = variantUnitRepository.findById(item.unitId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format(AppConstants.ERROR_MESSAGES.UNIT_NOT_FOUND, item.unitId())));

            if (variantUnit.getUnitType() == UnitType.SELL) {
                throw new BusinessValidationException(
                        String.format(AppConstants.ERROR_MESSAGES.INVALID_UNIT_TYPE, variantUnit.getUnitType()));
            }
        }
    }


    private void createPurchaseOutboxEvent(Purchase purchase) {

        PurchaseCreatedEvent event = new PurchaseCreatedEvent(
                purchase.getId(),
                purchase.getBillingType(),
                purchase.getMoneyTransaction(),
                purchase.getVatAmount(),
                purchase.getGrossTotal());

        String payload;

        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize purchase event", e);
        }

        OutboxEvent outboxEvent = outboxMapper.map(
                "PURCHASE",
                purchase.getId().toString(),
                OutboxEventType.CREATED,
                payload,
                kafkaProperties.getTopic("purchase-event"));

        outboxEventRepository.save(outboxEvent);
    }

}
