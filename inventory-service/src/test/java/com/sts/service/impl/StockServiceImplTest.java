package com.sts.service.impl;

import com.sts.dto.request.CreateStockCommand;
import com.sts.dto.request.StockAdjustmentCommand;
import com.sts.dto.response.StockResponse;
import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.StockUpdateEventFactory;
import com.sts.exception.DuplicateResourceException;
import com.sts.mapper.StockMapper;
import com.sts.model.stock.Stock;
import com.sts.model.stock.StockVariant;
import com.sts.model.stock.VariantUnit;
import com.sts.repository.StockRepository;
import com.sts.repository.StockVariantRepository;
import com.sts.repository.VariantUnitRepository;
import com.sts.service.resolver.ReferenceResolver;
import com.sts.service.resolver.VariantUnitResolver;
import com.sts.helper.event.DomainEventPublisher;
import com.sts.utils.constant.AppConstants;
import com.sts.utils.enums.StockType;
import com.sts.utils.enums.UnitType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockVariantRepository stockVariantRepository;

    @Mock
    private VariantUnitRepository variantUnitRepository;

    @Mock
    private StockUpdateEventFactory stockUpdateEventFactory;

    @Mock
    private StockMapper stockMapper;

    @Mock
    private ReferenceResolver referenceResolver;

    @Mock
    private VariantUnitResolver variantUnitResolver;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private StockServiceImpl stockService;

    UUID variantId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID unitId = UUID.fromString("22222222-2222-2222-2222-222222222222");


    CreateStockCommand createStockCommand() {
        CreateStockCommand.VariantUnitCommand unit = CreateStockCommand.VariantUnitCommand.builder()
                .name("caret")
                .conversionRate(BigDecimal.valueOf(24))
                .unitType(UnitType.BOTH)
                .build();

        CreateStockCommand.VariantItemCommand variant = CreateStockCommand.VariantItemCommand.builder()
                .name("coke 100 ml")
                .baseUnit("pic")
                .units(List.of(unit))
                .build();

        return CreateStockCommand.builder()
                .name("coke")
                .type(StockType.BEVERAGE)
                .variants(List.of(variant))
                .build();
    }

    StockAdjustmentCommand stockAdjustmentCommand() {
        return StockAdjustmentCommand.builder()
                .variantId(variantId)
                .unitId(unitId)
                .quantity(BigDecimal.valueOf(3))
                .reason("something")
                .build();
    }


    @Test
    void createStock_shouldReturnResponse_whenNameDoesNotExist() {
        CreateStockCommand command = createStockCommand();

        Stock mappedStock = mock(Stock.class);
        Stock savedStock = mock(Stock.class);
        StockResponse stockResponse = mock(StockResponse.class);

        when(stockRepository.existsByName(command.name())).thenReturn(false);
        when(stockMapper.buildStock(command)).thenReturn(mappedStock);
        when(stockRepository.save(mappedStock)).thenReturn(savedStock);
        when(stockMapper.toResponse(savedStock)).thenReturn(stockResponse);

        StockResponse stock = stockService.createStock(command);

        assertEquals(stockResponse, stock);
    }

    @Test
    void createStock_shouldThrowDuplicateResourceException_whenNameExists() {
        CreateStockCommand command = createStockCommand();
        when(stockRepository.existsByName(command.name())).thenReturn(true);

        DuplicateResourceException ex = assertThrows(DuplicateResourceException.class,
                () -> stockService.createStock(command));

        assertEquals(
                String.format(AppConstants.ErrorMessages.STOCK_ALREADY_EXISTS, command.name()),
                ex.getMessage()
        );

        verify(stockRepository).existsByName(command.name());
        verifyNoMoreInteractions(stockRepository, stockMapper, eventPublisher);

    }

    @Test
    void adjustStock_shouldPublishEvent_whenCommandIsValid() {
        StockAdjustmentCommand command = stockAdjustmentCommand();

        StockVariant stockVariant = mock(StockVariant.class);
        VariantUnit variantUnit = mock(VariantUnit.class);
        StockUpdateEvent event = mock(StockUpdateEvent.class);


        when(referenceResolver.getVariantOrThrow(variantId)).thenReturn(stockVariant);
        when(stockVariant.getId()).thenReturn(variantId);

        when(variantUnitResolver.getVariantUnitOrThrow(variantId, unitId)).thenReturn(variantUnit);
        when(variantUnit.getId()).thenReturn(unitId);

        when(stockUpdateEventFactory.buildFromAdjustment(variantId, unitId, BigDecimal.valueOf(3)))
                .thenReturn(event);


        stockService.adjustStock(command);

        verify(referenceResolver).getVariantOrThrow(variantId);
        verify(variantUnitResolver).getVariantUnitOrThrow(variantId, unitId);
        verify(stockUpdateEventFactory).buildFromAdjustment(variantId, unitId, BigDecimal.valueOf(3));
        verify(eventPublisher).publish(event);
    }
}