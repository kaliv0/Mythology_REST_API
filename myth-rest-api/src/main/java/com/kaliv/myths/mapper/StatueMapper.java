package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.statueDtos.CreateStatueDto;
import com.kaliv.myths.dto.statueDtos.StatueDto;
import com.kaliv.myths.dto.statueDtos.StatueResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Statue;

public class StatueMapper {
    private final ModelMapper mapper;

    @Autowired
    public StatueMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public StatueDto statueToDto(Statue statue) {
        StatueDto statueDto = mapper.map(statue, StatueDto.class);
        statueDto.setMythCharacterIds(
                statue.getMythCharacters().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        statueDto.setStatueImageIds(
                statue.getStatueImages().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        statueDto.setSmallStatueImageIds(
                statue.getSmallStatueImages().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return statueDto;
    }

    public StatueResponseDto statueToResponseDto(Statue statue) {
        StatueResponseDto statueResponseDto = mapper.map(statue, StatueResponseDto.class);
        statueResponseDto.setMythCharacters(
                statue.getMythCharacters().stream()
                        .map(character -> mapper.map(character, BaseDto.class))
                        .collect(Collectors.toSet()));
        statueResponseDto.setStatueImages(
                statue.getStatueImages().stream()
                        .map(image -> mapper.map(image, BaseDto.class))
                        .collect(Collectors.toSet()));
        statueResponseDto.setSmallStatueImages(
                statue.getSmallStatueImages().stream()
                        .map(image -> mapper.map(image, BaseDto.class))
                        .collect(Collectors.toSet()));
        return statueResponseDto;
    }

    public Statue dtoToStatue(CreateStatueDto dto) {
        return mapper.map(dto, Statue.class);
    }
}
