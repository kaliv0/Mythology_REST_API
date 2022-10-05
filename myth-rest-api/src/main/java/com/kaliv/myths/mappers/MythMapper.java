package com.kaliv.myths.mappers;

import com.kaliv.myths.dtos.mythDtos.MythDto;
import com.kaliv.myths.entities.Myth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MythMapper {

    MythMapper INSTANCE = Mappers.getMapper(MythMapper.class);

    @Mapping(target = "myth.characters", ignore = true)
    MythDto entityToDto(Myth myth);
    Myth dtoToEntity(MythDto dto);
}
