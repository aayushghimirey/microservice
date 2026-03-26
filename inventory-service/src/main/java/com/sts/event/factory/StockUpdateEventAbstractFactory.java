package com.sts.event.factory;

import com.sts.entity.OutboxEventType;
import com.sts.enums.AggregateType;
import com.sts.event.StockUpdateEvent;

public interface StockUpdateEventAbstractFactory  {

    StockUpdateEvent build(Object input);

}
