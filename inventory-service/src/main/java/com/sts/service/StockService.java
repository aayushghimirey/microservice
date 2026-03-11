package com.sts.service;

import java.util.UUID;

import com.sts.event.StockUpdateEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sts.dto.request.CreateStockCommand;
import com.sts.dto.request.GetStockQueryRequest;
import com.sts.dto.request.StockAdjustmentCommand;
import com.sts.dto.request.UpdateStockCommand;
import com.sts.dto.response.StockResponse;

public interface StockService {

    StockResponse createStock(CreateStockCommand command);

    StockResponse updateStock(UUID stockId, UpdateStockCommand updateCommand);

    void adjustStock(StockAdjustmentCommand command);

    // -- query -----------------
    Page<StockResponse> getAllStock(Pageable pageable);

    Page<StockResponse> getAllQueryStock(GetStockQueryRequest queryRequest, Pageable pageable);

    Page<StockResponse.VariantResponse> getAllVariantByStockId(UUID stockId, Pageable pageable);

    // id validators
    boolean existsByVariantIdAndUnitId(UUID variantId, UUID unitId);

    void processStockUpdates(StockUpdateEvent event);

}
