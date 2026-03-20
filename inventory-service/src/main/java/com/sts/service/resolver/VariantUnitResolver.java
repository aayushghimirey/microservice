package com.sts.service.resolver;

import java.util.UUID;

import com.sts.utils.constant.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.sts.exception.ResourceNotFoundException;
import com.sts.model.stock.VariantUnit;
import com.sts.repository.StockVariantRepository;
import com.sts.repository.VariantUnitRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Slf4j
public class VariantUnitResolver {

    private final StockVariantRepository stockVariantRepository;
    private final VariantUnitRepository variantUnitRepository;

    public VariantUnit getVariantUnitOrThrow(UUID variantId, UUID unitId) {
        if (!stockVariantRepository.existsById(variantId)) {
            log.error(String.format(AppConstants.ErrorMessages.VARIANT_NOT_FOUND, variantId));
            throw new ResourceNotFoundException(
                    String.format(AppConstants.ErrorMessages.VARIANT_NOT_FOUND, variantId)
            );
        }

        return variantUnitRepository.findByIdAndStockVariantId(unitId, variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(AppConstants.ErrorMessages.UNIT_NOT_FOUND, unitId)
                ));
    }
}
