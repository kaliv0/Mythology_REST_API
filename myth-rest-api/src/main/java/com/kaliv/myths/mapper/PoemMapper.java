package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.poemDtos.CreatePoemDto;
import com.kaliv.myths.dto.poemDtos.PoemDto;
import com.kaliv.myths.dto.poemDtos.PoemResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Poem;

public class PoemMapper {
    private final ModelMapper mapper;

    public PoemMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public PoemDto poemToDto(Poem poem) {
        PoemDto poemDto = mapper.map(poem, PoemDto.class);
        poemDto.setMythCharacterIds(
                poem.getMythCharacters().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return poemDto;
    }

    public PoemResponseDto poemToResponseDto(Poem poem) {
        PoemResponseDto poemResponseDto = mapper.map(poem, PoemResponseDto.class);
        poemResponseDto.setMythCharacters(
                poem.getMythCharacters().stream()
                        .map(character -> mapper.map(character, BaseDto.class))
                        .collect(Collectors.toSet()));
        return poemResponseDto;
    }

    public Poem dtoToPoem(CreatePoemDto dto) {
        return mapper.map(dto, Poem.class);
    }
}
