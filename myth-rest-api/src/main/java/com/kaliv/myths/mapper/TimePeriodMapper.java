package com.kaliv.myths.mapper;

import org.springframework.stereotype.Component;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.timePeriodDtos.CreateTimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.entity.TimePeriod;

@Component
public class TimePeriodMapper {
    private final GenericMapper mapper;

    public TimePeriodMapper(GenericMapper mapper) {
        this.mapper = mapper;
    }

    public TimePeriodDto timePeriodToDto(TimePeriod timePeriod) {
        TimePeriodDto timePeriodDto = mapper.entityToDto(timePeriod, TimePeriodDto.class);
        timePeriodDto.setAuthors(mapper.mapNestedEntities(timePeriod.getAuthors(), BaseDto.class));
        return timePeriodDto;
    }

    public TimePeriod dtoToTimePeriod(CreateTimePeriodDto dto) {
        return mapper.dtoToEntity(dto, TimePeriod.class);
    }
}
