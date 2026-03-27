package com.sts.service;

import com.sts.event.StockUpdateEvent;

public interface StockUpdateProcessor {

    void process(StockUpdateEvent event);

}
