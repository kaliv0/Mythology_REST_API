package com.kaliv.myths.service.myth;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kaliv.myths.common.criteria.PaginationCriteria;
import com.kaliv.myths.common.criteria.SortCriteria;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.mythDtos.*;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Myth;
import com.kaliv.myths.entity.MythCharacter;
import com.kaliv.myths.entity.Nationality;
import com.kaliv.myths.exception.DuplicateEntriesException;
import com.kaliv.myths.exception.alreadyExists.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.MythMapper;
import com.kaliv.myths.persistence.MythCharacterRepository;
import com.kaliv.myths.persistence.MythRepository;
import com.kaliv.myths.persistence.NationalityRepository;

@Service
public class MythServiceImpl implements MythService {

    private final MythRepository mythRepository;
    private final NationalityRepository nationalityRepository;
    private final MythCharacterRepository mythCharacterRepository;
    private final MythMapper mapper;

    public MythServiceImpl(MythRepository mythRepository,
                           NationalityRepository nationalityRepository,
                           MythCharacterRepository mythCharacterRepository,
                           MythMapper mapper) {
        this.mythRepository = mythRepository;
        this.nationalityRepository = nationalityRepository;
        this.mythCharacterRepository = mythCharacterRepository;
        this.mapper = mapper;
    }

    @Override
    public PaginatedMythResponseDto getAllMyths(PaginationCriteria paginationCriteria, SortCriteria sortCriteria) {
        int page = paginationCriteria.getPage();
        int size = paginationCriteria.getSize();
        String sortDir = sortCriteria.getSortOrder();
        String sortAttr = sortCriteria.getSortAttribute();

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortAttr);
        Page<Myth> myths = mythRepository.findAll(pageable);
        List<Myth> listOfMyths = myths.getContent(); //TODO:check if redundant

        List<MythResponseDto> content = listOfMyths.stream()
                .map(mapper::mythToResponseDto)
                .collect(Collectors.toList());

        PaginatedMythResponseDto mythResponseDto = new PaginatedMythResponseDto();
        mythResponseDto.setContent(content);
        mythResponseDto.setPageNumber(myths.getNumber());
        mythResponseDto.setPageSize(myths.getSize());
        mythResponseDto.setTotalElements(myths.getTotalElements());
        mythResponseDto.setTotalPages(myths.getTotalPages());
        mythResponseDto.setLast(myths.isLast());
        return mythResponseDto;
    }

    @Override
    public MythResponseDto getMythById(long id) {
        Myth mythInDb = mythRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MYTH, Fields.ID, id));
        return mapper.mythToResponseDto(mythInDb);
    }

    @Override
    public MythDto createMyth(CreateMythDto dto) {
        String name = dto.getName();
        if (mythRepository.existsByName(name)) {
            throw new ResourceWithGivenValuesExistsException(Sources.MYTH, Fields.NAME, name);
        }

        Long nationalityId = dto.getNationalityId();
        if (nationalityId != null && !nationalityRepository.existsById(nationalityId)) {
            throw new ResourceWithGivenValuesNotFoundException(Sources.NATIONALITY, Fields.ID, nationalityId);
        }

        List<Long> mythCharacterIds = new ArrayList<>(dto.getMythCharacterIds());
        List<MythCharacter> mythCharacters = mythCharacterRepository.findAllById(mythCharacterIds);
        if (mythCharacters.size() != mythCharacterIds.size()) {
            throw new ResourceListNotFoundException(Sources.CHARACTERS, Fields.IDS);
        }

        Myth myth = mapper.dtoToMyth(dto);
        Myth savedMyth = mythRepository.save(myth);

        mythCharacters.forEach(ch -> ch.getMyths().add(savedMyth));
        mythCharacterRepository.saveAll(mythCharacters);
        return mapper.mythToDto(savedMyth);
    }

    @Override
    public MythDto updateMyth(long id, UpdateMythDto dto) {
        Myth mythInDb = mythRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MYTH, Fields.ID, id));

        if (Optional.ofNullable(dto.getName()).isPresent()) {
            String newName = dto.getName();
            if (!newName.equals(mythInDb.getName()) && mythRepository.existsByName(newName)) {
                throw new ResourceWithGivenValuesExistsException(Sources.MYTH, Fields.NAME, newName);
            }
            mythInDb.setName(dto.getName());
        }

        if (Optional.ofNullable(dto.getNationalityId()).isPresent()) {
            long nationalityId = dto.getNationalityId();
            Nationality nationalityInDb = nationalityRepository.findById(nationalityId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.NATIONALITY, Fields.ID, nationalityId));
            mythInDb.setNationality(nationalityInDb);
        }

        List<Long> mythCharactersToAddIds = new ArrayList<>(dto.getMythCharactersToAdd());
        List<Long> mythCharactersToRemoveIds = new ArrayList<>(dto.getMythsCharactersToRemove());
        //check if user tries to add and remove same myth character
        if (!Collections.disjoint(mythCharactersToAddIds, mythCharactersToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_CHARACTERS, Sources.REMOVE_CHARACTERS);
        }
        //check if user tries to add myth character that is already in the list
        if (mythInDb.getMythCharacters().stream()
                .map(BaseEntity::getId)
                .anyMatch(mythCharactersToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.CHARACTER);
        }
        //check if user tries to remove myth character that is not in the list
        if (!mythInDb.getMythCharacters().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet())
                .containsAll(mythCharactersToRemoveIds)) {
            throw new ResourceNotFoundException(Sources.CHARACTER);
        }

        List<MythCharacter> mythCharactersToAdd = mythCharacterRepository.findAllById(mythCharactersToAddIds);
        List<MythCharacter> mythCharactersToRemove = mythCharacterRepository.findAllById(mythCharactersToRemoveIds);
        if (mythCharactersToAddIds.size() != mythCharactersToAdd.size()
                || mythCharactersToRemoveIds.size() != mythCharactersToRemove.size()) {
            throw new ResourceListNotFoundException(Sources.CHARACTERS, Fields.IDS);
        }

        mythInDb.getMythCharacters().addAll(new HashSet<>(mythCharactersToAdd));
        mythInDb.getMythCharacters().removeAll(new HashSet<>(mythCharactersToRemove));
        mythRepository.save(mythInDb);

        mythCharactersToAdd.forEach(a -> a.getMyths().add(mythInDb));
        mythCharacterRepository.saveAll(mythCharactersToAdd);
        mythCharactersToRemove.forEach(a -> a.getMyths().remove(mythInDb));
        mythCharacterRepository.saveAll(mythCharactersToRemove);

        return mapper.mythToDto(mythInDb);
    }

    @Override
    public void deleteMyth(long id) {
        Myth myth = mythRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MYTH, Fields.ID, id));
        mythRepository.delete(myth);
    }
}
