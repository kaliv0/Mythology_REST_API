package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.mythCharacterDtos.CreateMythCharacterDto;
import com.kaliv.myths.dto.mythCharacterDtos.MythCharacterDto;
import com.kaliv.myths.dto.mythCharacterDtos.MythCharacterResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.MythCharacter;

public class MythCharacterMapper {
    private final ModelMapper mapper;

    @Autowired
    public MythCharacterMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public MythCharacterDto mythCharacterToDto(MythCharacter mythCharacter) {
        MythCharacterDto mythCharacterDto = mapper.map(mythCharacter, MythCharacterDto.class);
        mythCharacterDto.setMythIds(
                mythCharacter.getMyths().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        mythCharacterDto.setStatueIds(
                mythCharacter.getStatues().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        mythCharacterDto.setPaintingIds(
                mythCharacter.getPaintings().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        mythCharacterDto.setMusicIds(
                mythCharacter.getMusic().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        mythCharacterDto.setPoemIds(
                mythCharacter.getPoems().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return mythCharacterDto;
    }

    public MythCharacterResponseDto mythCharacterToResponseDto(MythCharacter mythCharacter) {
        MythCharacterResponseDto mythCharacterResponseDto = mapper.map(mythCharacter, MythCharacterResponseDto.class);
        mythCharacterResponseDto.setMyths(
                mythCharacter.getMyths().stream()
                        .map(myth -> mapper.map(myth, BaseDto.class))
                        .collect(Collectors.toSet()));
        mythCharacterResponseDto.setStatues(
                mythCharacter.getStatues().stream()
                        .map(statue -> mapper.map(statue, BaseDto.class))
                        .collect(Collectors.toSet()));
        mythCharacterResponseDto.setPaintings(
                mythCharacter.getPaintings().stream()
                        .map(painting -> mapper.map(painting, BaseDto.class))
                        .collect(Collectors.toSet()));
        mythCharacterResponseDto.setMusic(
                mythCharacter.getMusic().stream()
                        .map(music -> mapper.map(music, BaseDto.class))
                        .collect(Collectors.toSet()));
        mythCharacterResponseDto.setPoems(
                mythCharacter.getPoems().stream()
                        .map(poem -> mapper.map(poem, BaseDto.class))
                        .collect(Collectors.toSet()));
        return mythCharacterResponseDto;
    }

    public MythCharacter dtoToMythCharacter(CreateMythCharacterDto dto) {
        return mapper.map(dto, MythCharacter.class);
    }
}
