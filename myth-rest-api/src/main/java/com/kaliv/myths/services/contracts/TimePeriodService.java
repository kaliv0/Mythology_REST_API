package com.kaliv.myths.services.contracts;

import com.kaliv.myths.dtos.timePeriodDtos.CreateTimePeriodDto;
import com.kaliv.myths.dtos.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.dtos.timePeriodDtos.UpdateTimePeriodDto;

import java.util.List;

public interface TimePeriodService {
    List<TimePeriodDto> getAllTimePeriods();

    TimePeriodDto getTimePeriodById(long id);

    TimePeriodDto createTimePeriod(CreateTimePeriodDto dto);

    TimePeriodDto updateTimePeriod(long id, UpdateTimePeriodDto dto);

    void deleteTimePeriod(long id);
}
