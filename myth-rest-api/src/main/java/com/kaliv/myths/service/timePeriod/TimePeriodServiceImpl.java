package com.kaliv.myths.service.timePeriod;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.timePeriodDtos.CreateTimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodResponseDto;
import com.kaliv.myths.dto.timePeriodDtos.UpdateTimePeriodDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.TimePeriod;
import com.kaliv.myths.entity.artefacts.Author;
import com.kaliv.myths.exception.DuplicateEntriesException;
import com.kaliv.myths.exception.alreadyExists.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
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
    public List<TimePeriodResponseDto> getAllTimePeriods() {
        return timePeriodRepository.findAll()
                .stream().map(mapper::timePeriodToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public TimePeriodResponseDto getTimePeriodById(long id) {
        TimePeriod timePeriodInDb = timePeriodRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.TIME_PERIOD, Fields.ID, id));
        return mapper.timePeriodToResponseDto(timePeriodInDb);
    }

    @Override
    public TimePeriodDto createTimePeriod(CreateTimePeriodDto dto) {
        String name = dto.getName();
        if (timePeriodRepository.existsByName(name)) {
            throw new ResourceWithGivenValuesExistsException(Sources.TIME_PERIOD, Fields.NAME, name);
        }

        List<Long> authorIds = new ArrayList<>(dto.getAuthorIds());
        List<Author> authors = authorRepository.findAllById(authorIds);
        if (authors.size() != authorIds.size()) {
            throw new ResourceListNotFoundException(Sources.AUTHORS, Fields.IDS);
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
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.TIME_PERIOD, Fields.ID, id));

        if (Optional.ofNullable(dto.getName()).isPresent()) {
            String newName = dto.getName();
            if (!newName.equals(timePeriodInDb.getName()) && authorRepository.existsByName(newName)) {
                throw new ResourceWithGivenValuesExistsException(Sources.TIME_PERIOD, Fields.NAME, newName);
            }
            timePeriodInDb.setName(dto.getName());
        }

        Optional.ofNullable(dto.getYears()).ifPresent(timePeriodInDb::setYears);

        List<Long> authorsToAddIds = new ArrayList<>(dto.getAuthorsToAdd());
        List<Long> authorsToRemoveIds = new ArrayList<>(dto.getAuthorsToRemove());
        //check if user tries to add and remove same author
        if (!Collections.disjoint(authorsToAddIds, authorsToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_AUTHORS, Sources.REMOVE_AUTHORS);
        }
        //check if user tries to add author that is already in the list
        if (timePeriodInDb.getAuthors().stream()
                .map(BaseEntity::getId)
                .anyMatch(authorsToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.AUTHOR);
        }
        //check if user tries to remove author that is not in the list
        if (!timePeriodInDb.getAuthors().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet())
                .containsAll(authorsToRemoveIds)) {
            throw new ResourceNotFoundException(Sources.AUTHOR);
        }

        List<Author> authorsToAdd = authorRepository.findAllById(authorsToAddIds);
        List<Author> authorsToRemove = authorRepository.findAllById(authorsToRemoveIds);
        if (authorsToAddIds.size() != authorsToAdd.size()
                || authorsToRemoveIds.size() != authorsToRemove.size()) {
            throw new ResourceListNotFoundException(Sources.AUTHORS, Fields.IDS);
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
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.TIME_PERIOD, Fields.ID, id));
        timePeriodRepository.delete(timePeriodInDb);
    }
}
