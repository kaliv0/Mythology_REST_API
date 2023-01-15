package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.museumDtos.CreateMuseumDto;
import com.kaliv.myths.dto.museumDtos.MuseumDto;
import com.kaliv.myths.dto.museumDtos.MuseumResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Museum;

@Component
public class MuseumMapper {
    private final GenericMapper mapper;

    public MuseumMapper(GenericMapper mapper) {
        this.mapper = mapper;
    }

    public MuseumDto museumToDto(Museum museum) {
        MuseumDto museumDto = mapper.entityToDto(museum, MuseumDto.class);
        museumDto.setStatueIds(
                museum.getStatues().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        museumDto.setPaintingIds(
                museum.getPaintings().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return museumDto;
    }

    public MuseumResponseDto museumToResponseDto(Museum museum) {
        MuseumResponseDto museumResponseDto = mapper.entityToDto(museum, MuseumResponseDto.class);
        museumResponseDto.setStatues(mapper.mapNestedEntities(museum.getStatues(), BaseDto.class));
        museumResponseDto.setPaintings(mapper.mapNestedEntities(museum.getPaintings(), BaseDto.class));
        return museumResponseDto;
    }

    public Museum dtoToMuseum(CreateMuseumDto dto) {
        return mapper.dtoToEntity(dto, Museum.class);
    }
}
