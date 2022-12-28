package com.kaliv.myths.service.timePeriod;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.dto.timePeriodDtos.CreateTimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.UpdateTimePeriodDto;
import com.kaliv.myths.entity.TimePeriod;
import com.kaliv.myths.entity.artefacts.Author;
import com.kaliv.myths.exception.DuplicateEntriesException;
import com.kaliv.myths.exception.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.ResourceListNotFoundException;
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
    public TimePeriodDto createTimePeriod(CreateTimePeriodDto dto) {
        String name = dto.getName();
        if (timePeriodRepository.existsByName(name)) {
            throw new ResourceAlreadyExistsException("Time period", "name", name);
        }

        //ACID transaction => successful only if all given values are valid
        List<Long> authorIds = new ArrayList<>(dto.getAuthorIds());
        List<Author> authors = authorRepository.findAllById(authorIds);

        if (authors.size() != authorIds.size()) {
            throw new ResourceListNotFoundException("Authors", "ids");
        }

        TimePeriod timePeriod = mapper.dtoToTimePeriod(dto);
        TimePeriod savedTimePeriod = timePeriodRepository.save(timePeriod);

        authors.forEach(a -> a.setTimePeriod(savedTimePeriod));
        authorRepository.saveAll(authors);
        return mapper.timePeriodToDto(savedTimePeriod);
    }

    @Override
    public TimePeriodDto updateTimePeriod(long id, UpdateTimePeriodDto dto) {
        TimePeriod timePeriodInDb = timePeriodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time period", "id", id));

        if (Optional.ofNullable(dto.getName()).isPresent()) {
            String name = dto.getName();
            if (!name.equals(timePeriodInDb.getName()) && authorRepository.existsByName(name)) {
                throw new ResourceAlreadyExistsException("Time period", "name", name);
            }
            timePeriodInDb.setName(dto.getName());
        }

        Optional.ofNullable(dto.getYears()).ifPresent(timePeriodInDb::setYears);

        List<Long> authorsToAddIds = new ArrayList<>(dto.getAuthorsToAdd());
        List<Long> authorsToRemoveIds = new ArrayList<>(dto.getAuthorsToRemove());

        if (!Collections.disjoint(authorsToAddIds, authorsToRemoveIds)) {
            throw new DuplicateEntriesException("authorsToAdd", "authorsToRemove");
        }

        List<Author> authorsToAdd = authorRepository.findAllById(authorsToAddIds);
        List<Author> authorsToRemove = authorRepository.findAllById(authorsToRemoveIds);

        if (authorsToAddIds.size() != authorsToAdd.size()
                || authorsToRemoveIds.size() != authorsToRemove.size()) {
            throw new ResourceListNotFoundException("Authors", "ids");
        }

        timePeriodInDb.getAuthors().addAll(new HashSet<>(authorsToAdd));
        timePeriodInDb.getAuthors().removeAll(new HashSet<>(authorsToRemove));

        timePeriodRepository.save(timePeriodInDb);

        authorsToAdd.forEach(a -> a.setTimePeriod(timePeriodInDb));
        authorRepository.saveAll(authorsToAdd);

        authorsToRemove.forEach(a -> a.setTimePeriod(null));
        authorRepository.saveAll(authorsToRemove);

        return mapper.timePeriodToDto(timePeriodInDb);
    }

    @Override
    public void deleteTimePeriod(long id) {
        TimePeriod timePeriodInDb = timePeriodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time period", "id", id));
        timePeriodRepository.delete(timePeriodInDb);
    }
}
