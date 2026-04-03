package com.sts.event;


public interface EventProcessingStrategy<E> {

    void process(E e);

}
