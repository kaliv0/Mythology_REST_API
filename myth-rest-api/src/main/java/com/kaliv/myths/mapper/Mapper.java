package com.kaliv.myths.mapper;

public interface Mapper {
    <T, E> E entityToDto(T entity, Class<E> clazz);
}
