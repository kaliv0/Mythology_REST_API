package com.kaliv.myths.service.myth;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kaliv.myths.common.ArtworkHandler;
import com.kaliv.myths.common.container.Quadruple;
import com.kaliv.myths.common.container.Tuple;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.mythDtos.*;
import com.kaliv.myths.entity.*;
import com.kaliv.myths.entity.artefacts.Music;
import com.kaliv.myths.entity.artefacts.Painting;
import com.kaliv.myths.entity.artefacts.Poem;
import com.kaliv.myths.entity.artefacts.Statue;
import com.kaliv.myths.exception.alreadyExists.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.invalidInput.DuplicateEntriesException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.MythMapper;
import com.kaliv.myths.persistence.MythCharacterRepository;
import com.kaliv.myths.persistence.MythRepository;
import com.kaliv.myths.persistence.NationalityRepository;
import com.querydsl.core.BooleanBuilder;

@Service
public class MythServiceImpl implements MythService {

    private final MythRepository mythRepository;
    private final NationalityRepository nationalityRepository;
    private final MythCharacterRepository mythCharacterRepository;
    private final ArtworkHandler artworkHandler;
    private final MythMapper mapper;

    public MythServiceImpl(MythRepository mythRepository,
                           NationalityRepository nationalityRepository,
                           MythCharacterRepository mythCharacterRepository,
                           ArtworkHandler artworkHandler,
                           MythMapper mapper) {
        this.mythRepository = mythRepository;
        this.nationalityRepository = nationalityRepository;
        this.mythCharacterRepository = mythCharacterRepository;
        this.artworkHandler = artworkHandler;
        this.mapper = mapper;
    }

    @Override
    public PaginatedMythResponseDto getAllMyths(String nationalityName,
                                                String characterName,
                                                int pageNumber,
                                                int pageSize,
                                                String sortBy,
                                                String sortOrder) {
        QMyth qMyth = QMyth.myth;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (nationalityName != null) {
            booleanBuilder.and(qMyth.nationality.name.equalsIgnoreCase(nationalityName));
        }
        if (characterName != null) {
            booleanBuilder.and(qMyth.mythCharacters.any().name.equalsIgnoreCase(characterName));
        }

        Sort sortCriteria = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortCriteria);
        Page<Myth> myths = mythRepository.findAll(booleanBuilder, pageable);

        List<MythResponseDto> content = myths
                .getContent().stream()
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

        Quadruple<List<Statue>, List<Painting>, List<Music>, List<Poem>> artworks = artworkHandler.getValidArtworks(dto);
        List<Statue> statues = artworks.getFirst();
        List<Painting> paintings = artworks.getSecond();
        List<Music> music = artworks.getThird();
        List<Poem> poems = artworks.getFourth();

        Myth myth = mapper.dtoToMyth(dto);
        myth.setMythCharacters(new HashSet<>(mythCharacters));
        myth.setStatues(new HashSet<>(statues));
        myth.setPaintings(new HashSet<>(paintings));
        myth.setMusic(new HashSet<>(music));
        myth.setPoems(new HashSet<>(poems));
        Myth savedMyth = mythRepository.save(myth);

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

        Tuple<List<MythCharacter>, List<MythCharacter>> mythCharactersToUpdate = this.getValidMythCharacters(dto, mythInDb);
        List<MythCharacter> mythCharactersToAdd = mythCharactersToUpdate.getFirst();
        List<MythCharacter> mythCharactersToRemove = mythCharactersToUpdate.getSecond();
        mythInDb.getMythCharacters().addAll(new HashSet<>(mythCharactersToAdd));
        mythInDb.getMythCharacters().removeAll(new HashSet<>(mythCharactersToRemove));

        artworkHandler.handleArtworksToUpdate(dto, mythInDb);
        mythRepository.save(mythInDb);
        return mapper.mythToDto(mythInDb);
    }

    @Override
    public void deleteMyth(long id) {
        Myth myth = mythRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MYTH, Fields.ID, id));
        mythRepository.delete(myth);
    }

    private Tuple<List<MythCharacter>, List<MythCharacter>> getValidMythCharacters(UpdateMythDto dto, Myth mythInDb) {
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
        return new Tuple<>(mythCharactersToAdd, mythCharactersToRemove);
    }
}
