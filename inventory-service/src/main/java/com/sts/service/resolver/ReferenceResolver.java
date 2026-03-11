package com.sts.service.resolver;

import com.sts.exception.ResourceNotFoundException;
import com.sts.model.stock.Stock;
import com.sts.model.stock.StockVariant;
import com.sts.model.vendor.Vendor;
import com.sts.repository.StockRepository;
import com.sts.repository.StockVariantRepository;
import com.sts.repository.VendorRepository;
import com.sts.utils.constant.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReferenceResolver {

    private final StockRepository stockRepository;
    private final VendorRepository vendorRepository;
    private final StockVariantRepository stockVariantRepository;

    public Vendor getVendorOrThrow(UUID vendorId) {
        return orThrow(
                vendorRepository.findById(vendorId),
                String.format(AppConstants.ERROR_MESSAGES.VENDOR_NOT_FOUND, vendorId)
        );
    }

    public Stock getStockOrThrow(UUID stockId) {
        return orThrow(
                stockRepository.findById(stockId),
                String.format(AppConstants.ERROR_MESSAGES.STOCK_NOT_FOUND, stockId)
        );
    }

    public StockVariant getVariantOrThrow(UUID variantId) {
        return orThrow(
                stockVariantRepository.findById(variantId),
                String.format(AppConstants.ERROR_MESSAGES.VARIANT_NOT_FOUND, variantId)
        );
    }

    private <T> T orThrow(Optional<T> entity, String message) {
        return entity.orElseThrow(() -> new ResourceNotFoundException(message));
    }
}
