package com.kaliv.myths.service.timePeriod;

import com.kaliv.myths.dto.timePeriodDtos.CreateUpdateTimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodDto;

import java.util.List;

public interface TimePeriodService {
    List<TimePeriodDto> getAllTimePeriods();

    TimePeriodDto getTimePeriodById(long id);

    TimePeriodDto createTimePeriod(CreateUpdateTimePeriodDto dto);

    TimePeriodDto updateTimePeriod(long id, CreateUpdateTimePeriodDto dto);

    void deleteTimePeriod(long id);
}
