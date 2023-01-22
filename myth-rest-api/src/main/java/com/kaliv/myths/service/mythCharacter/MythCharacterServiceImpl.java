package com.kaliv.myths.service.mythCharacter;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kaliv.myths.common.criteria.PaginationCriteria;
import com.kaliv.myths.common.criteria.SortCriteria;
import com.kaliv.myths.common.utils.Tuple;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.mythCharacterDtos.*;
import com.kaliv.myths.entity.*;
import com.kaliv.myths.exception.alreadyExists.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.invalidInput.DuplicateEntriesException;
import com.kaliv.myths.exception.invalidInput.InvalidParentException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.MythCharacterMapper;
import com.kaliv.myths.persistence.CategoryRepository;
import com.kaliv.myths.persistence.MythCharacterRepository;
import com.kaliv.myths.persistence.MythRepository;
import com.querydsl.core.BooleanBuilder;

@Service
public class MythCharacterServiceImpl implements MythCharacterService {
    private final MythCharacterRepository mythCharacterRepository;
    private final CategoryRepository categoryRepository;
    private final MythRepository mythRepository;
    private final MythCharacterMapper mapper;

    public MythCharacterServiceImpl(MythCharacterRepository mythCharacterRepository,
                                    CategoryRepository categoryRepository,
                                    MythRepository mythRepository,
                                    MythCharacterMapper mapper) {
        this.mythCharacterRepository = mythCharacterRepository;
        this.categoryRepository = categoryRepository;
        this.mythRepository = mythRepository;
        this.mapper = mapper;
    }

    @Override
    public PaginatedMythCharacterResponseDto getAllMythCharacters(String fatherName,
                                                                  String motherName,
                                                                  String categoryName,
                                                                  String mythName,
                                                                  PaginationCriteria paginationCriteria,
                                                                  SortCriteria sortCriteria) {
        QMythCharacter qMythCharacter = QMythCharacter.mythCharacter;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (fatherName != null) {
            booleanBuilder.and(qMythCharacter.father.name.equalsIgnoreCase(fatherName));
        }
        if (motherName != null) {
            booleanBuilder.and(qMythCharacter.mother.name.equalsIgnoreCase(motherName));
        }
        if (categoryName != null) {
            booleanBuilder.and(qMythCharacter.category.name.equalsIgnoreCase(categoryName));
        }
        if (mythName != null) {
            booleanBuilder.and(qMythCharacter.myths.any().name.equalsIgnoreCase(mythName));
        }

        int page = paginationCriteria.getPage();
        int size = paginationCriteria.getSize();
        String sortDir = sortCriteria.getSortOrder();
        String sortAttr = sortCriteria.getSortAttribute();
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortAttr);
        Page<MythCharacter> mythCharacters = mythCharacterRepository.findAll(booleanBuilder, pageable);

        List<MythCharacterResponseDto> content = mythCharacters
                .getContent() //TODO:check if redundant
                .stream()
                .map(mapper::mythCharacterToResponseDto)
                .collect(Collectors.toList());

        PaginatedMythCharacterResponseDto mythCharacterResponseDto = new PaginatedMythCharacterResponseDto();
        mythCharacterResponseDto.setContent(content);
        mythCharacterResponseDto.setPageNumber(mythCharacters.getNumber());
        mythCharacterResponseDto.setPageSize(mythCharacters.getSize());
        mythCharacterResponseDto.setTotalElements(mythCharacters.getTotalElements());
        mythCharacterResponseDto.setTotalPages(mythCharacters.getTotalPages());
        mythCharacterResponseDto.setLast(mythCharacters.isLast());

        return mythCharacterResponseDto;
    }

    @Override
    public MythCharacterResponseDto getMythCharacterById(long id) {
        MythCharacter mythCharacterInDb = mythCharacterRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.CHARACTER, Fields.ID, id));
        return mapper.mythCharacterToResponseDto(mythCharacterInDb);
    }

    @Override
    public MythCharacterDto createMythCharacter(CreateMythCharacterDto dto) {
        String name = dto.getName();
        if (mythCharacterRepository.existsByName(name)) {
            throw new ResourceWithGivenValuesExistsException(Sources.CHARACTER, Fields.NAME, name);
        }

        Long categoryId = dto.getCategoryId();
        if (categoryId != null && !categoryRepository.existsById(categoryId)) {
            throw new ResourceWithGivenValuesNotFoundException(Sources.CATEGORY, Fields.ID, categoryId);
        }

        Long fatherId = dto.getFatherId();
        MythCharacter father = null;
        if (fatherId != null) {
            father = mythCharacterRepository.findById(fatherId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.CHARACTER, Fields.ID, fatherId));
        }

        Long motherId = dto.getMotherId();
        MythCharacter mother = null;
        if (motherId != null) {
            if (Objects.equals(fatherId, motherId)) {
                throw new DuplicateEntriesException(Fields.FATHER_ID, Fields.MOTHER_ID);
            }
            mother = mythCharacterRepository.findById(motherId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.CHARACTER, Fields.ID, motherId));
        }

        List<Long> mythIds = new ArrayList<>(dto.getMythIds());
        List<Myth> myths = mythRepository.findAllById(mythIds);
        if (myths.size() != mythIds.size()) {
            throw new ResourceListNotFoundException(Sources.MYTHS, Fields.IDS);
        }

        MythCharacter mythCharacter = mapper.dtoToMythCharacter(dto);
        mythCharacter.setFather(father);
        mythCharacter.setMother(mother);
        MythCharacter savedMythCharacter = mythCharacterRepository.save(mythCharacter);

        myths.forEach(m -> m.getMythCharacters().add(savedMythCharacter));
        mythRepository.saveAll(myths);
        return mapper.mythCharacterToDto(savedMythCharacter);
    }

    @Override
    public MythCharacterDto updateMythCharacter(long id, UpdateMythCharacterDto dto) {
        MythCharacter mythCharacterInDb = mythCharacterRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.CHARACTER, Fields.ID, id));

        if (Optional.ofNullable(dto.getName()).isPresent()) {
            String newName = dto.getName();
            if (!newName.equals(mythCharacterInDb.getName()) && mythCharacterRepository.existsByName(newName)) {
                throw new ResourceWithGivenValuesExistsException(Sources.CHARACTER, Fields.NAME, newName);
            }
            mythCharacterInDb.setName(dto.getName());
        }

        if (Optional.ofNullable(dto.getCategoryId()).isPresent()) {
            long categoryId = dto.getCategoryId();
            Category categoryInDb = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.CATEGORY, Fields.ID, categoryId));
            mythCharacterInDb.setCategory(categoryInDb);
        }

        Long fatherId = dto.getFatherId();
        MythCharacter father = null;
        if (fatherId != null) {
            father = mythCharacterRepository.findById(fatherId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.CHARACTER, Fields.ID, fatherId));
        }

        Long motherId = dto.getMotherId();
        MythCharacter mother = null;
        if (motherId != null) {
            if (Objects.equals(fatherId, motherId)) {
                throw new DuplicateEntriesException(Fields.FATHER_ID, Fields.MOTHER_ID);
            }
            if (Objects.equals(fatherId, id) || Objects.equals(motherId, id)) {
                throw new InvalidParentException();
            }
            mother = mythCharacterRepository.findById(motherId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.CHARACTER, Fields.ID, motherId));
        }
        mythCharacterInDb.setFather(father);
        mythCharacterInDb.setMother(mother);

        Tuple<List<Myth>, List<Myth>> mythsToUpdate = this.getValidMyths(dto, mythCharacterInDb);
        List<Myth> mythsToAdd = mythsToUpdate.getFirst();
        List<Myth> mythsToRemove = mythsToUpdate.getSecond();
        mythCharacterInDb.getMyths().addAll(new HashSet<>(mythsToAdd));
        mythCharacterInDb.getMyths().removeAll(new HashSet<>(mythsToRemove));
        mythCharacterRepository.save(mythCharacterInDb);

        mythsToAdd.forEach(a -> a.getMythCharacters().add(mythCharacterInDb));
        mythRepository.saveAll(mythsToAdd);
        mythsToRemove.forEach(a -> a.getMythCharacters().remove(mythCharacterInDb));
        mythRepository.saveAll(mythsToRemove);

        return mapper.mythCharacterToDto(mythCharacterInDb);
    }

    @Override
    public void deleteMythCharacter(long id) {
        MythCharacter mythCharacterInDb = mythCharacterRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.CHARACTERS, Fields.ID, id));
        mythCharacterRepository.delete(mythCharacterInDb);
    }

    private Tuple<List<Myth>, List<Myth>> getValidMyths(UpdateMythCharacterDto dto, MythCharacter mythCharacterInDb) {
        List<Long> mythsToAddIds = new ArrayList<>(dto.getMythsToAdd());
        List<Long> mythsToRemoveIds = new ArrayList<>(dto.getMythsToRemove());
        //check if user tries to add and remove same myth
        if (!Collections.disjoint(mythsToAddIds, mythsToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_MYTHS, Sources.REMOVE_MYTHS);
        }
        //check if user tries to add myth that is already in the list
        if (mythCharacterInDb.getMyths().stream()
                .map(BaseEntity::getId)
                .anyMatch(mythsToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.MYTH);
        }
        //check if user tries to remove myth that is not in the list
        if (!mythCharacterInDb.getMyths().stream()
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
}
