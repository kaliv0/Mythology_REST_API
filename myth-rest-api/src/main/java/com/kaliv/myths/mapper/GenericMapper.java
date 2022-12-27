package com.kaliv.myths.mapper;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.kaliv.myths.entity.BaseEntity;

@Component
public class GenericMapper {
    private final ModelMapper mapper = new ModelMapper();

    public <T, U> U entityToDto(T entity, Class<U> dtoClass) {
        return dtoClass.cast(mapper.map(entity, dtoClass));
    }

    public <T, U extends BaseEntity> U dtoToEntity(T dto, Class<U> entityClass) {
//        mapper.getConfiguration().setSkipNullEnabled(true);

        mapper.emptyTypeMap(dto.getClass(), entityClass)
                .addMappings(m -> m.skip(BaseEntity::setId))
                .implicitMappings();
        return entityClass.cast(mapper.map(dto, entityClass));
    }

    public <T extends BaseEntity> Set<Long> mapNestedEntities(Collection<T> entities) {
        return entities.stream().map(BaseEntity::getId).collect(Collectors.toSet());
    }
}
