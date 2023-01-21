package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.museumDtos.CreateMuseumDto;
import com.kaliv.myths.dto.museumDtos.MuseumDto;
import com.kaliv.myths.dto.museumDtos.MuseumResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Museum;

public class MuseumMapper {
    private final ModelMapper mapper;

    public MuseumMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public MuseumDto museumToDto(Museum museum) {
        MuseumDto museumDto = mapper.map(museum, MuseumDto.class);
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
        MuseumResponseDto museumResponseDto = mapper.map(museum, MuseumResponseDto.class);
        museumResponseDto.setStatues(
                museum.getStatues().stream()
                        .map(statue -> mapper.map(statue, BaseDto.class))
                        .collect(Collectors.toSet()));
        museumResponseDto.setPaintings(
                museum.getPaintings().stream()
                        .map(painting -> mapper.map(painting, BaseDto.class))
                        .collect(Collectors.toSet()));
        return museumResponseDto;
    }

    public Museum dtoToMuseum(CreateMuseumDto dto) {
        return mapper.map(dto, Museum.class);
    }
}
