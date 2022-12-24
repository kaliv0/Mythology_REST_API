package com.kaliv.myths.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MythMapperOpt
//        implements Mapper
{

    final private ModelMapper mapper = new ModelMapper();

//    @Override
    public <T, E> E entityToDto(T entity, Class<E> clazz) {
        return clazz.cast(mapper.map(entity, clazz));
    }
}
