package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.poemDtos.CreatePoemDto;
import com.kaliv.myths.dto.poemDtos.PoemDto;
import com.kaliv.myths.dto.poemDtos.PoemResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Poem;

@Component
public class PoemMapper {
    private final GenericMapper mapper;

    public PoemMapper(GenericMapper mapper) {
        this.mapper = mapper;
    }

    public PoemDto poemToDto(Poem poem) {
        PoemDto poemDto = mapper.entityToDto(poem, PoemDto.class);
        poemDto.setMythCharacterIds(
                poem.getMythCharacters().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return poemDto;
    }

    public PoemResponseDto poemToResponseDto(Poem poem) {
        PoemResponseDto poemResponseDto = mapper.entityToDto(poem, PoemResponseDto.class);
        poemResponseDto.setMythCharacters(mapper.mapNestedEntities(poem.getMythCharacters(), BaseDto.class));
        return poemResponseDto;
    }

    public Poem dtoToPoem(CreatePoemDto dto) {
        return mapper.dtoToEntity(dto, Poem.class);
    }
}
