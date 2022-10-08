package com.kaliv.myths.service.timePeriod;

import com.kaliv.myths.dto.timePeriodDtos.CreateTimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.UpdateTimePeriodDto;
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
