package com.sts.event.factory;

import com.sts.event.StockUpdateEvent;

public interface StockUpdateEventFactory<T> {
    StockUpdateEvent build(T input);
}