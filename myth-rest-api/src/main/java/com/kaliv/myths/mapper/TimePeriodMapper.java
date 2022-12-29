package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.timePeriodDtos.CreateTimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.TimePeriod;

@Component
public class TimePeriodMapper {
    private final GenericMapper mapper;

    public TimePeriodMapper(GenericMapper mapper) {
        this.mapper = mapper;
    }

    public TimePeriodDto timePeriodToDto(TimePeriod timePeriod) {
        TimePeriodDto timePeriodDto = mapper.entityToDto(timePeriod, TimePeriodDto.class);
        timePeriodDto.setAuthorIds(
                timePeriod.getAuthors().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return timePeriodDto;
    }

    public TimePeriodResponseDto timePeriodToResponseDto(TimePeriod timePeriod) {
        TimePeriodResponseDto timePeriodResponseDto = mapper.entityToDto(timePeriod, TimePeriodResponseDto.class);
        timePeriodResponseDto.setAuthors(mapper.mapNestedEntities(timePeriod.getAuthors(), BaseDto.class));
        return timePeriodResponseDto;
    }

    public TimePeriod dtoToTimePeriod(CreateTimePeriodDto dto) {
        return mapper.dtoToEntity(dto, TimePeriod.class);
    }
}
