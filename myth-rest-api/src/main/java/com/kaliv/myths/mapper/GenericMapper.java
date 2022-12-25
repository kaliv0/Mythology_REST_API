package com.kaliv.myths.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GenericMapper {
    private final ModelMapper mapper = new ModelMapper();

    public <T, U> U entityToDto(T entity, Class<U> dtoClass) {
        return dtoClass.cast(mapper.map(entity, dtoClass));
    }

    public <T, U> U dtoToEntity(T dto, Class<U> entityClass) {
        return entityClass.cast(mapper.map(dto, entityClass));
    }
}
