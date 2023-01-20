package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.paintingDtos.CreatePaintingDto;
import com.kaliv.myths.dto.paintingDtos.PaintingDto;
import com.kaliv.myths.dto.paintingDtos.PaintingResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Painting;

@Component
public class PaintingMapper {
    private final GenericMapper mapper;

    public PaintingMapper(GenericMapper mapper) {
        this.mapper = mapper;
    }

    public PaintingDto paintingToDto(Painting painting) {
        PaintingDto paintingDto = mapper.entityToDto(painting, PaintingDto.class);
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
        PaintingResponseDto paintingResponseDto = mapper.entityToDto(painting, PaintingResponseDto.class);
        paintingResponseDto.setMythCharacters(mapper.mapNestedEntities(painting.getMythCharacters(), BaseDto.class));
        paintingResponseDto.setPaintingImages(mapper.mapNestedEntities(painting.getPaintingImages(), BaseDto.class));
        return paintingResponseDto;
    }

    public Painting dtoToPainting(CreatePaintingDto dto) {
        return mapper.dtoToEntity(dto, Painting.class);
    }
}
