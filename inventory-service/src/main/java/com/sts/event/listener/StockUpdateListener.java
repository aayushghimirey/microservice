package com.sts.event.listener;

import com.sts.service.StockUpdateProcessor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sts.event.StockUpdateEvent;

import lombok.RequiredArgsConstructor;

/*
 * Entry point for stock update
 * */
@Component
@RequiredArgsConstructor
public class StockUpdateListener {

    private final StockUpdateProcessor processor;

    @EventListener
    public void on(StockUpdateEvent event) {
        processor.process(event);
    }

}