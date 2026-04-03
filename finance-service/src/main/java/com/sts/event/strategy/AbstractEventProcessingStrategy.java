package com.sts.event.strategy;

import com.sts.event.EventProcessingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public abstract class AbstractEventProcessingStrategy<E> implements EventProcessingStrategy<E> {

    @Override
    @Transactional
    public void process(E event) {

        if (event == null) {
            log.warn("Event cannot be null - skipping");
            return;
        }

        save(event);
    }

    protected abstract void save(E event);


}
