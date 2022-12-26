package com.kaliv.myths.service.timePeriod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.dto.timePeriodDtos.CreateUpdateTimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.exception.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.ResourceNotFoundException;
import com.kaliv.myths.mapper.GenericMapper;
import com.kaliv.myths.entity.TimePeriod;
import com.kaliv.myths.entity.artefacts.Author;
import com.kaliv.myths.persistence.AuthorRepository;
import com.kaliv.myths.persistence.TimePeriodRepository;

@Service
public class TimePeriodServiceImpl implements TimePeriodService {

    private final TimePeriodRepository timePeriodRepository;
    private final AuthorRepository authorRepository;
    private final GenericMapper mapper;

    public TimePeriodServiceImpl(TimePeriodRepository timePeriodRepository, AuthorRepository authorRepository, GenericMapper mapper) {
        this.timePeriodRepository = timePeriodRepository;
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }

    @Override
    public List<TimePeriodDto> getAllTimePeriods() {
        return timePeriodRepository.findAll()
                .stream().map(timePeriod -> {
                    TimePeriodDto timePeriodDto = mapper.entityToDto(timePeriod, TimePeriodDto.class);
                    timePeriodDto.setAuthorIds(mapper.mapNestedEntities(timePeriod.getAuthors()));
                    return timePeriodDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public TimePeriodDto getTimePeriodById(long id) {
        TimePeriod timePeriodInDb = timePeriodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time period", "id", id));
        TimePeriodDto timePeriodDto = mapper.entityToDto(timePeriodInDb, TimePeriodDto.class);
        timePeriodDto.setAuthorIds(mapper.mapNestedEntities(timePeriodInDb.getAuthors()));
        return timePeriodDto;
    }

    @Override
    public TimePeriodDto createTimePeriod(CreateUpdateTimePeriodDto dto) {
        String name = dto.getName();
        if (timePeriodRepository.findByName(name).isPresent()) {
            throw new ResourceAlreadyExistsException("TimePeriod", "name", name);
        }

        TimePeriod timePeriod = mapper.dtoToEntity(dto, TimePeriod.class);

        final TimePeriod savedTimePeriod = timePeriodRepository.save(timePeriod);

        //add timePeriod to authors
        List<Long> authorIds = new ArrayList<>(dto.getAuthorIds());
        List<Author> autors = authorRepository.findAllById(authorIds);
        if (autors.size() != authorIds.size()) {
            throw new ResourceNotFoundException("Authors", "id", 0);
        }

        autors.forEach(a -> a.setTimePeriod(savedTimePeriod));
        authorRepository.saveAllAndFlush(autors);


        return mapper.entityToDto(timePeriod, TimePeriodDto.class);
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
