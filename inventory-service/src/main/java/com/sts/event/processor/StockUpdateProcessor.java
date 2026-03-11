package com.sts.event.processor;

import com.sts.utils.constant.AppConstants;
import org.springframework.stereotype.Component;
import com.sts.event.StockUpdateEvent;
import com.sts.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Responsibility: Parses the incoming StockUpdateEvent and delegates
 * to the StockService for business logic execution.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StockUpdateProcessor {

    private final StockService stockService;

    public void process(StockUpdateEvent event) {
        log.info(AppConstants.LOG_MESSAGES.PROCESSING_STOCK_UPDATE, event.items().size());

        stockService.processStockUpdates(event);
    }
}
