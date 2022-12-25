package com.kaliv.myths.service.timePeriod;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.dto.timePeriodDtos.CreateUpdateTimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.exception.ResourceNotFoundException;
import com.kaliv.myths.mapper.GenericMapper;
import com.kaliv.myths.model.TimePeriod;
import com.kaliv.myths.persistence.TimePeriodRepository;

@Service
public class TimePeriodServiceImpl implements TimePeriodService {

    private final TimePeriodRepository timePeriodRepository;
    private final GenericMapper mapper;

    public TimePeriodServiceImpl(TimePeriodRepository timePeriodRepository, GenericMapper mapper) {
        this.timePeriodRepository = timePeriodRepository;
        this.mapper = mapper;
    }

    @Override
    public List<TimePeriodDto> getAllTimePeriods() {
        return timePeriodRepository.findAll()
                .stream().map(timePeriod -> mapper.entityToDto(timePeriod, TimePeriodDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TimePeriodDto getTimePeriodById(long id) {
        TimePeriod timePeriodInDb = timePeriodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time period", "id", id));
        return mapper.entityToDto(timePeriodInDb, TimePeriodDto.class);
    }

    @Override
    public TimePeriodDto createTimePeriod(CreateUpdateTimePeriodDto dto) {
        return null;
    }

    @Override
    public TimePeriodDto updateTimePeriod(long id, CreateUpdateTimePeriodDto dto) {
        return null;
    }

    @Override
    public void deleteTimePeriod(long id) {
        TimePeriod timePeriodInDb = timePeriodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time period", "id", id));
        timePeriodRepository.delete(timePeriodInDb);
    }
}
