package com.sts.stock.api;


import com.sts.stock.command.CreateStockCommand;
import com.sts.stock.command.StockAdjustmentCommand;
import com.sts.stock.command.UpdateStockCommand;
import com.sts.stock.application.usecase.StockCommandUseCase;
import com.sts.stock.dto.StockResponse;
import com.sts.stock.application.mapper.StockMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/stock")
@AllArgsConstructor
public class StockCommandController {

    private final StockCommandUseCase stockCommandUseCase;
    private final StockMapper stockMapper;

    @PostMapping
    public ResponseEntity<StockResponse> createStock(@Valid @RequestBody CreateStockCommand command) {
        var stock = stockCommandUseCase.createStock(command);

        StockResponse response = stockMapper.toResponse(stock);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{stockId}")
    public ResponseEntity<StockResponse> updateStock(@PathVariable("stockId") UUID stockId, @Valid @RequestBody UpdateStockCommand command) {
        var stock = stockCommandUseCase.updateStock(stockId, command);

        StockResponse response = stockMapper.toResponse(stock);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/adjustment")
    public ResponseEntity<Void> createAdjustment(@RequestBody StockAdjustmentCommand command) {
        stockCommandUseCase.createAdjustmentStock(command);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}