package com.sts.event.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sts.event.StockUpdateEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockUpdateListener {

    private final StockUpdateProcessor processor;

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(StockUpdateEvent event) {
        log.info("Stock update event received");
        processor.process(event);
    }
}