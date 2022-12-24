package com.kaliv.myths.mapper;

import org.modelmapper.ModelMapper;

import com.kaliv.myths.dto.timePeriodDtos.CreateUpdateTimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.model.TimePeriod;


public class TimePeriodMapper {

    final private static ModelMapper mapper = new ModelMapper();

    public static TimePeriodDto timePeriodToDto(TimePeriod timePeriod) {
        return mapper.map(timePeriod, TimePeriodDto.class);
    }

    public static TimePeriod dtoToTimePeriod(CreateUpdateTimePeriodDto timePeriod) {
        return mapper.map(timePeriod, TimePeriod.class);
    }
}
