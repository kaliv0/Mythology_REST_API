package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.statueDtos.CreateStatueDto;
import com.kaliv.myths.dto.statueDtos.StatueDto;
import com.kaliv.myths.dto.statueDtos.StatueResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Statue;

@Component
public class StatueMapper {
    private final GenericMapper mapper;

    public StatueMapper(GenericMapper mapper) {
        this.mapper = mapper;
    }

    public StatueDto statueToDto(Statue statue) {
        StatueDto statueDto = mapper.entityToDto(statue, StatueDto.class);
        statueDto.setMythCharacterIds(
                statue.getMythCharacters().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        statueDto.setStatueImageIds(
                statue.getStatueImages().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return statueDto;
    }

    public StatueResponseDto statueToResponseDto(Statue statue) {
        StatueResponseDto statueResponseDto = mapper.entityToDto(statue, StatueResponseDto.class);
        statueResponseDto.setMythCharacters(mapper.mapNestedEntities(statue.getMythCharacters(), BaseDto.class));
        statueResponseDto.setStatueImages(mapper.mapNestedEntities(statue.getStatueImages(), BaseDto.class));
        return statueResponseDto;
    }

    public Statue dtoToStatue(CreateStatueDto dto) {
        return mapper.dtoToEntity(dto, Statue.class);
    }
}
