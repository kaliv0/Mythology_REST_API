package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.paintingDtos.CreatePaintingDto;
import com.kaliv.myths.dto.paintingDtos.PaintingDto;
import com.kaliv.myths.dto.paintingDtos.PaintingResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Painting;

public class PaintingMapper {
    private final ModelMapper mapper;

    public PaintingMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public PaintingDto paintingToDto(Painting painting) {
        PaintingDto paintingDto = mapper.map(painting, PaintingDto.class);
        paintingDto.setMythCharacterIds(
                painting.getMythCharacters().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        paintingDto.setPaintingImageIds(
                painting.getPaintingImages().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return paintingDto;
    }

    public PaintingResponseDto paintingToResponseDto(Painting painting) {
        PaintingResponseDto paintingResponseDto = mapper.map(painting, PaintingResponseDto.class);
        paintingResponseDto.setMythCharacters(
                painting.getMythCharacters()
                        .stream().map(character -> mapper.map(character, BaseDto.class))
                        .collect(Collectors.toSet()));
        paintingResponseDto.setPaintingImages(
                painting.getPaintingImages().stream()
                        .map(image -> mapper.map(image, BaseDto.class))
                        .collect(Collectors.toSet()));
        return paintingResponseDto;
    }

    public Painting dtoToPainting(CreatePaintingDto dto) {
        return mapper.map(dto, Painting.class);
    }
}
