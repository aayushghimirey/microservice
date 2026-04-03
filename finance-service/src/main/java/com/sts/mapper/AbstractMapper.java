package com.sts.mapper;

public interface AbstractMapper<E, R, D> {

    E buildEntity(D d);

    R toResponse(E e);

}
