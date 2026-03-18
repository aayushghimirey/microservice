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

    public PurchaseRecordResponse toResponse(PurchaseRecord record) {
        return PurchaseRecordResponse.builder()
                .id(record.getId())
                .purchaseId(record.getPurchaseId())
                .billingType(record.getBillingType())
                .vatAmount(record.getVatAmount())
                .grossTotal(record.getGrossTotal()).build();
    }

}
