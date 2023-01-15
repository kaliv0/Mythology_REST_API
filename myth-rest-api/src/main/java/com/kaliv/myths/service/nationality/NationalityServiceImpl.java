package com.kaliv.myths.service.nationality;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.common.utils.Tuple;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.nationalityDtos.CreateNationalityDto;
import com.kaliv.myths.dto.nationalityDtos.NationalityDto;
import com.kaliv.myths.dto.nationalityDtos.NationalityResponseDto;
import com.kaliv.myths.dto.nationalityDtos.UpdateNationalityDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Myth;
import com.kaliv.myths.entity.Nationality;
import com.kaliv.myths.entity.artefacts.Author;
import com.kaliv.myths.exception.alreadyExists.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.invalidInput.DuplicateEntriesException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.NationalityMapper;
import com.kaliv.myths.persistence.AuthorRepository;
import com.kaliv.myths.persistence.MythRepository;
import com.kaliv.myths.persistence.NationalityRepository;

@Service
public class NationalityServiceImpl implements NationalityService {

    private final NationalityRepository nationalityRepository;
    private final MythRepository mythRepository;
    private final AuthorRepository authorRepository;
    private final NationalityMapper mapper;

    public NationalityServiceImpl(NationalityRepository nationalityRepository,
                                  MythRepository mythRepository,
                                  AuthorRepository authorRepository,
                                  NationalityMapper mapper) {
        this.nationalityRepository = nationalityRepository;
        this.mythRepository = mythRepository;
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }

    @Override
    public List<NationalityResponseDto> getAllNationalities() {
        return nationalityRepository.findAll()
                .stream().map(mapper::nationalityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public NationalityResponseDto getNationalityById(long id) {
        Nationality nationalityInDb = nationalityRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.NATIONALITY, Fields.ID, id));
        return mapper.nationalityToResponseDto(nationalityInDb);
    }

    @Override
    public NationalityDto createNationality(CreateNationalityDto dto) {
        String name = dto.getName();
        if (nationalityRepository.existsByName(name)) {
            throw new ResourceWithGivenValuesExistsException(Sources.NATIONALITY, Fields.NAME, name);
        }

        List<Long> mythIds = new ArrayList<>(dto.getMythIds());
        List<Myth> myths = mythRepository.findAllById(mythIds);
        if (myths.size() != mythIds.size()) {
            throw new ResourceListNotFoundException(Sources.MYTHS, Fields.IDS);
        }

        List<Long> authorIds = new ArrayList<>(dto.getAuthorIds());
        List<Author> authors = authorRepository.findAllById(authorIds);
        if (authors.size() != authorIds.size()) {
            throw new ResourceListNotFoundException(Sources.AUTHORS, Fields.IDS);
        }

        Nationality nationality = mapper.dtoToNationality(dto);
        Nationality savedNationality = nationalityRepository.save(nationality);

        myths.forEach(m -> m.setNationality(savedNationality));
        mythRepository.saveAll(myths);
        authors.forEach(a -> a.setNationality(savedNationality));
        authorRepository.saveAll(authors);

        return mapper.nationalityToDto(savedNationality);
    }

    @Override
    public NationalityDto updateNationality(long id, UpdateNationalityDto dto) {
        Nationality nationalityInDb = nationalityRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.NATIONALITY, Fields.ID, id));

        if (Optional.ofNullable(dto.getName()).isPresent()) {
            String newName = dto.getName();
            if (!newName.equals(nationalityInDb.getName()) && nationalityRepository.existsByName(newName)) {
                throw new ResourceWithGivenValuesExistsException(Sources.NATIONALITY, Fields.NAME, newName);
            }
            nationalityInDb.setName(dto.getName());
        }

        Tuple<List<Myth>, List<Myth>> mythsToUpdate = this.getValidMyths(dto, nationalityInDb);
        List<Myth> mythsToAdd = mythsToUpdate.getFirst();
        List<Myth> mythsToRemove = mythsToUpdate.getSecond();
        nationalityInDb.getMyths().addAll(new HashSet<>(mythsToAdd));
        nationalityInDb.getMyths().removeAll(new HashSet<>(mythsToRemove));

        Tuple<List<Author>, List<Author>> authorsToUpdate = this.getValidAuthors(dto, nationalityInDb);
        List<Author> authorsToAdd = authorsToUpdate.getFirst();
        List<Author> authorsToRemove = authorsToUpdate.getSecond();
        nationalityInDb.getAuthors().addAll(new HashSet<>(authorsToAdd));
        nationalityInDb.getAuthors().removeAll(new HashSet<>(authorsToRemove));

        nationalityRepository.save(nationalityInDb);

        mythsToAdd.forEach(m -> m.setNationality(nationalityInDb));
        mythRepository.saveAll(mythsToAdd);
        mythsToRemove.forEach(m -> m.setNationality(null));
        mythRepository.saveAll(mythsToRemove);

        authorsToAdd.forEach(a -> a.setNationality(nationalityInDb));
        authorRepository.saveAll(authorsToAdd);
        authorsToRemove.forEach(a -> a.setNationality(null));
        authorRepository.saveAll(authorsToRemove);

        return mapper.nationalityToDto(nationalityInDb);
    }

    @Override
    public void deleteNationality(long id) {
        Nationality nationalityInDb = nationalityRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.NATIONALITY, Fields.ID, id));
        nationalityRepository.delete(nationalityInDb);
    }


    private Tuple<List<Myth>, List<Myth>> getValidMyths(UpdateNationalityDto dto, Nationality nationalityInDb) {
        List<Long> mythsToAddIds = new ArrayList<>(dto.getMythsToAdd());
        List<Long> mythsToRemoveIds = new ArrayList<>(dto.getMythsToRemove());
        //check if user tries to add and remove same myth
        if (!Collections.disjoint(mythsToAddIds, mythsToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_MYTHS, Sources.REMOVE_MYTHS);
        }
        //check if user tries to add myth that is already in the list
        if (nationalityInDb.getMyths().stream()
                .map(BaseEntity::getId)
                .anyMatch(mythsToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.MYTH);
        }
        //check if user tries to remove myth that is not in the list
        if (!nationalityInDb.getMyths().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet())
                .containsAll(mythsToRemoveIds)) {
            throw new ResourceNotFoundException(Sources.MYTH);
        }

        List<Myth> mythsToAdd = mythRepository.findAllById(mythsToAddIds);
        List<Myth> mythsToRemove = mythRepository.findAllById(mythsToRemoveIds);
        if (mythsToAddIds.size() != mythsToAdd.size()
                || mythsToRemoveIds.size() != mythsToRemove.size()) {
            throw new ResourceListNotFoundException(Sources.MYTHS, Fields.IDS);
        }
        return new Tuple<>(mythsToAdd, mythsToRemove);
    }

    private Tuple<List<Author>, List<Author>> getValidAuthors(UpdateNationalityDto dto, Nationality nationalityInDb) {
        List<Long> authorsToAddIds = new ArrayList<>(dto.getAuthorsToAdd());
        List<Long> authorsToRemoveIds = new ArrayList<>(dto.getAuthorsToRemove());
        //check if user tries to add and remove same author
        if (!Collections.disjoint(authorsToAddIds, authorsToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_AUTHORS, Sources.REMOVE_AUTHORS);
        }
        //check if user tries to add author that is already in the list
        if (nationalityInDb.getAuthors().stream()
                .map(BaseEntity::getId)
                .anyMatch(authorsToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.AUTHOR);
        }
        //check if user tries to remove author that is not in the list
        if (!nationalityInDb.getAuthors().stream()
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
        return new Tuple<>(authorsToAdd, authorsToRemove);
    }
}
