package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.mythDtos.CreateMythDto;
import com.kaliv.myths.dto.mythDtos.MythDto;
import com.kaliv.myths.dto.mythDtos.MythResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Myth;

public class MythMapper {
    private final ModelMapper mapper;

    public MythMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public MythDto mythToDto(Myth myth) {
        MythDto mythDto = mapper.map(myth, MythDto.class);
        mythDto.setMythCharacterIds(
                myth.getMythCharacters().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return mythDto;
    }

    public MythResponseDto mythToResponseDto(Myth myth) {
        MythResponseDto mythResponseDto = mapper.map(myth, MythResponseDto.class);
        mythResponseDto.setMythCharacters(
                myth.getMythCharacters().stream()
                        .map(character -> mapper.map(character, BaseDto.class))
                        .collect(Collectors.toSet()));
        return mythResponseDto;
    }

    public Myth dtoToMyth(CreateMythDto dto) {
        return mapper.map(dto, Myth.class);
    }
}
