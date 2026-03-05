package com.sts.dto.response;


import com.sts.constant.enums.TransactionReference;
import com.sts.model.stock.StockTransaction;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Builder
public class StockTransactionResponse {

    private UUID id;
    private UUID referenceId;
    private TransactionReference referenceType;

    private UUID variantId;
    private String variantName;

    private UUID unitId;
    private String unitName;

    private BigDecimal quantityChange;
    private BigDecimal balanceAfter;
    private String remark;


    public static StockTransactionResponse fromEntity(
            StockTransaction entity,
            String variantName,
            String unitName
    ) {
        return StockTransactionResponse.builder()
                .id(entity.getId())
                .referenceId(entity.getReferenceId())
                .referenceType(entity.getReferenceType())
                .variantId(entity.getVariantId())
                .variantName(variantName)
                .unitId(entity.getUnitId())
                .unitName(unitName)
                .quantityChange(entity.getQuantityChange())
                .balanceAfter(entity.getBalanceAfter())
                .remark(entity.getRemark())
                .build();
    }


}
