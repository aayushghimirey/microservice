package com.sts.mapper;

import com.sts.dto.PurchaseRecordResponse;
import com.sts.event.PurchaseCreatedEvent;
import com.sts.model.PurchaseRecord;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PurchaseRecordMapper {

    public PurchaseRecord buildPurchaseRecord(PurchaseCreatedEvent event) {
        return PurchaseRecord.builder()
                .purchaseId(event.getPurchaseId())
                .billingType(event.getBillingType())
                .moneyTransaction(event.getMoneyTransaction())
                .vatAmount(event.getVatAmount() != null ? event.getVatAmount() : BigDecimal.ZERO)
                .grossTotal(event.getGrossTotal())
                .build();
    }

    public PurchaseRecordResponse toResponse(PurchaseRecord entity) {
        return PurchaseRecordResponse.builder()
                .id(entity.getId())
                .purchaseId(entity.getPurchaseId())
                .billingType(entity.getBillingType())
                .vatAmount(entity.getVatAmount())
                .createdDateTime(entity.getCreatedDateTime())
                .grossTotal(entity.getGrossTotal()).build();
    }

}
