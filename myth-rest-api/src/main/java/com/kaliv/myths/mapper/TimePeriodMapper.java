package com.kaliv.myths.mapper;

import org.springframework.stereotype.Component;

import com.kaliv.myths.dto.timePeriodDtos.CreateUpdateTimePeriodDto;
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
        timePeriodDto.setAuthorIds(mapper.mapNestedEntities(timePeriod.getAuthors()));
        return timePeriodDto;
    }

    public TimePeriod dtoToTimePeriod(CreateUpdateTimePeriodDto dto) {
        return mapper.dtoToEntity(dto, TimePeriod.class);
    }
}
