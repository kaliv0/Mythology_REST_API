package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.nationalityDtos.CreateNationalityDto;
import com.kaliv.myths.dto.nationalityDtos.NationalityDto;
import com.kaliv.myths.dto.nationalityDtos.NationalityResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Nationality;

@Component
public class NationalityMapper {
    private final GenericMapper mapper;

    public NationalityMapper(GenericMapper mapper) {
        this.mapper = mapper;
    }

    public NationalityDto nationalityToDto(Nationality nationality) {
        NationalityDto nationalityDto = mapper.entityToDto(nationality, NationalityDto.class);
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
        NationalityResponseDto nationalityResponseDto = mapper.entityToDto(nationality, NationalityResponseDto.class);
        nationalityResponseDto.setMyths(mapper.mapNestedEntities(nationality.getMyths(), BaseDto.class));
        nationalityResponseDto.setAuthors(mapper.mapNestedEntities(nationality.getAuthors(), BaseDto.class));
        return nationalityResponseDto;
    }

    public Nationality dtoToNationality(CreateNationalityDto dto) {
        return mapper.dtoToEntity(dto, Nationality.class);
    }
}
