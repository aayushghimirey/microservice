package com.sts.service;

import com.sts.dto.request.CreateStockCommand;
import com.sts.dto.request.GetStockQueryRequest;
import com.sts.dto.request.StockAdjustmentCommand;
import com.sts.dto.request.UpdateStockCommand;
import com.sts.dto.response.StockResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StockService {

    StockResponse createStock(CreateStockCommand command);

    StockResponse updateStock(UUID stockId, UpdateStockCommand updateCommand);

    void adjustStock(StockAdjustmentCommand command);


    Page<StockResponse> getAllQueryStock(GetStockQueryRequest queryRequest, Pageable pageable);

    Page<StockResponse.VariantResponse> getAllVariantByStockId(UUID stockId, Pageable pageable);

    // id validators
    boolean existsByVariantIdAndUnitId(UUID variantId, UUID unitId);


}
