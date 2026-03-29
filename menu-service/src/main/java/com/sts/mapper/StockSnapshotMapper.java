package com.sts.mapper;

import com.sts.event.StockEvent;
import com.sts.model.StockSnapshot;
import com.sts.model.VariantSnapshot;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class StockSnapshotMapper {

    public StockSnapshot toStockSnapshot(StockEvent event) {
        StockSnapshot stockSnapshot = new StockSnapshot();
        stockSnapshot.setStockId(event.stockId());

        event.variants().forEach(variantEvent -> {
            VariantSnapshot variantSnapshot = toVariantSnapshot(variantEvent);
            stockSnapshot.addVariant(variantSnapshot);
        });

        return stockSnapshot;
    }

    private VariantSnapshot toVariantSnapshot(StockEvent.VariantStockEvent variantSnapshot) {
        return VariantSnapshot.builder()
                .variantId(variantSnapshot.variantId())
                .unitIds(Set.copyOf(variantSnapshot.unitIds()))
                .build();
    }

}
