package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.mythDtos.CreateMythDto;
import com.kaliv.myths.dto.mythDtos.MythDto;
import com.kaliv.myths.dto.mythDtos.MythResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Myth;

@Component
public class MythMapper {
    private final GenericMapper mapper;

    public MythMapper(GenericMapper mapper) {
        this.mapper = mapper;
    }

    public MythDto mythToDto(Myth myth) {
        MythDto mythDto = mapper.entityToDto(myth, MythDto.class);
        mythDto.setMythCharacterIds(
                myth.getMythCharacters().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return mythDto;
    }

    public MythResponseDto mythToResponseDto(Myth myth) {
        MythResponseDto mythResponseDto = mapper.entityToDto(myth, MythResponseDto.class);
        mythResponseDto.setMythCharacters(mapper.mapNestedEntities(myth.getMythCharacters(), BaseDto.class));
        return mythResponseDto;
    }

    public Myth dtoToMyth(CreateMythDto dto) {
        return mapper.dtoToEntity(dto, Myth.class);
    }
}
