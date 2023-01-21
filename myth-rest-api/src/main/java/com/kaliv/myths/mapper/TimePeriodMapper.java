package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.timePeriodDtos.CreateTimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.TimePeriod;

public class TimePeriodMapper {
    private final ModelMapper mapper;

    public TimePeriodMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public TimePeriodDto timePeriodToDto(TimePeriod timePeriod) {
        TimePeriodDto timePeriodDto = mapper.map(timePeriod, TimePeriodDto.class);
        timePeriodDto.setAuthorIds(timePeriod.getAuthors().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet()));
        return timePeriodDto;
    }

    public TimePeriodResponseDto timePeriodToResponseDto(TimePeriod timePeriod) {
        TimePeriodResponseDto timePeriodResponseDto = mapper.map(timePeriod, TimePeriodResponseDto.class);
        timePeriodResponseDto.setAuthors(
                timePeriod.getAuthors().stream()
                        .map(author -> mapper.map(author, BaseDto.class))
                        .collect(Collectors.toSet()));
        return timePeriodResponseDto;
    }

    public TimePeriod dtoToTimePeriod(CreateTimePeriodDto dto) {
        return mapper.map(dto, TimePeriod.class);
    }
}
