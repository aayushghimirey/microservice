package com.sts.mapper;

import com.sts.event.StockEvent;
import com.sts.model.StockSnapshot;
import com.sts.model.UnitSnapshot;
import com.sts.model.VariantSnapshot;
import org.springframework.stereotype.Component;

@Component
public class StockSnapshotMapper {

    public StockSnapshot buildStockSnapshotFromEvent(StockEvent event) {
        if (event == null) return null;
        StockSnapshot snapshot = new StockSnapshot();
        snapshot.setStockId(event.id());
        snapshot.setName(event.name());
        snapshot.setType(event.type());
        event.variants().forEach(variant -> {
            VariantSnapshot variantSnapshot = new VariantSnapshot();
            variantSnapshot.setVariantId(variant.id());
            variantSnapshot.setName(variant.name());
            variantSnapshot.setBaseUnit(variant.baseUnit());
            variantSnapshot.setOpeningStock(variant.openingStock());
            variantSnapshot.setCurrentStock(variant.currentStock());
            variant.units().forEach(unit -> {
                UnitSnapshot unitSnapshot = new UnitSnapshot();
                unitSnapshot.setUnitId(unit.id());
                unitSnapshot.setName(unit.name());
                unitSnapshot.setConversionRate(unit.conversionRate());
                unitSnapshot.setUnitType(unit.unitType());
                variantSnapshot.addUnit(unitSnapshot);
            });
            snapshot.addVariant(variantSnapshot);
        });
        return snapshot;
    }
}
