package com.kaliv.myths.services.impl;

import com.kaliv.myths.dtos.timePeriodDtos.CreateTimePeriodDto;
import com.kaliv.myths.dtos.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.dtos.timePeriodDtos.UpdateTimePeriodDto;
import com.kaliv.myths.services.contracts.TimePeriodService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimePeriodServiceImpl implements TimePeriodService {
    @Override
    public List<TimePeriodDto> getAllTimePeriods() {
        return null;
    }

    @Override
    public TimePeriodDto getTimePeriodById(long id) {
        return null;
    }

    @Override
    public TimePeriodDto createTimePeriod(CreateTimePeriodDto dto) {
        return null;
    }

    @Override
    public TimePeriodDto updateTimePeriod(long id, UpdateTimePeriodDto dto) {
        return null;
    }

    @Override
    public void deleteTimePeriod(long id) {

    }
}
