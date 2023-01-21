package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.nationalityDtos.CreateNationalityDto;
import com.kaliv.myths.dto.nationalityDtos.NationalityDto;
import com.kaliv.myths.dto.nationalityDtos.NationalityResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Nationality;

public class NationalityMapper {
    private final ModelMapper mapper;

    public NationalityMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public NationalityDto nationalityToDto(Nationality nationality) {
        NationalityDto nationalityDto = mapper.map(nationality, NationalityDto.class);
        nationalityDto.setMythIds(
                nationality.getMyths().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        nationalityDto.setAuthorIds(
                nationality.getAuthors().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return nationalityDto;
    }

    public NationalityResponseDto nationalityToResponseDto(Nationality nationality) {
        NationalityResponseDto nationalityResponseDto = mapper.map(nationality, NationalityResponseDto.class);
        nationalityResponseDto.setMyths(
                nationality.getMyths().stream()
                        .map(myth -> mapper.map(myth, BaseDto.class))
                        .collect(Collectors.toSet()));
        nationalityResponseDto.setAuthors(
                nationality.getAuthors().stream()
                        .map(author -> mapper.map(author, BaseDto.class))
                        .collect(Collectors.toSet()));
        return nationalityResponseDto;
    }

    public Nationality dtoToNationality(CreateNationalityDto dto) {
        return mapper.map(dto, Nationality.class);
    }
}
