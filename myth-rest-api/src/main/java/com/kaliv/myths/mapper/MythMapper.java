package com.kaliv.myths.mapper;

import com.kaliv.myths.dto.mythDtos.MythDto;
import com.kaliv.myths.model.Myth;
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
