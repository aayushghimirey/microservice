package com.sts.support;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.sts.exception.ResourceNotFoundException;
import com.sts.model.stock.VariantUnit;
import com.sts.repository.StockVariantRepository;
import com.sts.repository.VariantUnitRepository;
import com.sts.utils.contant.AppConstants;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VariantUnitResolver {

    private final StockVariantRepository stockVariantRepository;
    private final VariantUnitRepository variantUnitRepository;

    public VariantUnit getVariantUnitOrThrow(UUID variantId, UUID unitId) {
        if (!stockVariantRepository.existsById(variantId)) {
            throw new ResourceNotFoundException(
                    String.format(AppConstants.ERROR_MESSAGES.VARIANT_NOT_FOUND, variantId)
            );
        }

        return variantUnitRepository.findByIdAndStockVariantId(unitId, variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(AppConstants.ERROR_MESSAGES.UNIT_NOT_FOUND, unitId)
                ));
    }
}
