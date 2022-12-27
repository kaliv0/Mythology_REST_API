package com.kaliv.myths.service.timePeriod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.dto.timePeriodDtos.CreateUpdateTimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.entity.TimePeriod;
import com.kaliv.myths.entity.artefacts.Author;
import com.kaliv.myths.exception.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.ResourceNotFoundException;
import com.kaliv.myths.mapper.TimePeriodMapper;
import com.kaliv.myths.persistence.AuthorRepository;
import com.kaliv.myths.persistence.TimePeriodRepository;

@Service
public class TimePeriodServiceImpl implements TimePeriodService {

    private final TimePeriodRepository timePeriodRepository;
    private final AuthorRepository authorRepository;
    private final TimePeriodMapper mapper;

    public TimePeriodServiceImpl(TimePeriodRepository timePeriodRepository,
                                 AuthorRepository authorRepository,
                                 TimePeriodMapper mapper) {
        this.timePeriodRepository = timePeriodRepository;
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }

    @Override
    public List<TimePeriodDto> getAllTimePeriods() {
        return timePeriodRepository.findAll()
                .stream().map(mapper::timePeriodToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TimePeriodDto getTimePeriodById(long id) {
        TimePeriod timePeriodInDb = timePeriodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time period", "id", id));
        return mapper.timePeriodToDto(timePeriodInDb);
    }

    @Override
    public TimePeriodDto createTimePeriod(CreateUpdateTimePeriodDto dto) {
        String name = dto.getName();
        if (timePeriodRepository.findByName(name).isPresent()) {
            throw new ResourceAlreadyExistsException("TimePeriod", "name", name);
        }

        TimePeriod timePeriod = mapper.dtoToTimePeriod(dto);
        TimePeriod savedTimePeriod = timePeriodRepository.save(timePeriod);

        //add timePeriod to authors
        List<Long> authorIds = new ArrayList<>(dto.getAuthorIds());
        List<Author> authors = authorRepository.findAllById(authorIds);

        //ACID transaction => successful only if all given values are valid ??
        if (authors.size() != authorIds.size()) {
            throw new ResourceNotFoundException("Authors", "id", 0);
        }

        authors.forEach(a -> a.setTimePeriod(savedTimePeriod));
        authorRepository.saveAll(authors);
        return mapper.timePeriodToDto(timePeriod);
    }

    @Override
    public TimePeriodDto updateTimePeriod(long id, CreateUpdateTimePeriodDto dto) {
        String name = dto.getName();
        if (timePeriodRepository.findByName(name).isPresent()) {
            throw new ResourceAlreadyExistsException("TimePeriod", "name", name);
        }

        TimePeriod timePeriod = mapper.dtoToTimePeriod(dto);
        TimePeriod savedTimePeriod = timePeriodRepository.save(timePeriod);

        //add timePeriod to authors
        List<Long> authorIds = new ArrayList<>(dto.getAuthorIds());
        List<Author> authors = authorRepository.findAllById(authorIds);

        //ACID transaction => successful only if all given values are valid
        if (authors.size() != authorIds.size()) {
            throw new ResourceNotFoundException("Authors", "id", 0);
        }

        authors.forEach(a -> a.setTimePeriod(savedTimePeriod));
        authorRepository.saveAll(authors);
        return mapper.timePeriodToDto(timePeriod);
    }

    @Override
    public void deleteTimePeriod(long id) {
        TimePeriod timePeriodInDb = timePeriodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time period", "id", id));
        timePeriodRepository.delete(timePeriodInDb);
    }
}
