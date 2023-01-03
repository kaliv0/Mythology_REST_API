package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.mythCharacterDtos.CreateMythCharacterDto;
import com.kaliv.myths.dto.mythCharacterDtos.MythCharacterDto;
import com.kaliv.myths.dto.mythCharacterDtos.MythCharacterResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.MythCharacter;

@Component
public class MythCharacterMapper {
    private final GenericMapper mapper;

    public MythCharacterMapper(GenericMapper mapper) {
        this.mapper = mapper;
    }

    public MythCharacterDto mythCharacterToDto(MythCharacter mythCharacter) {
        MythCharacterDto mythCharacterDto = mapper.entityToDto(mythCharacter, MythCharacterDto.class);
        mythCharacterDto.setMythIds(
                mythCharacter.getMyths().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return mythCharacterDto;
    }

    public MythCharacterResponseDto mythCharacterToResponseDto(MythCharacter mythCharacter) {
        MythCharacterResponseDto mythCharacterResponseDto = mapper.entityToDto(mythCharacter, MythCharacterResponseDto.class);
        mythCharacterResponseDto.setMyths(mapper.mapNestedEntities(mythCharacter.getMyths(), BaseDto.class));
        return mythCharacterResponseDto;
    }

    public MythCharacter dtoToMythCharacter(CreateMythCharacterDto dto) {
        return mapper.dtoToEntity(dto, MythCharacter.class);
    }
}
