package com.kaliv.myths.service.myth;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kaliv.myths.common.Quadruple;
import com.kaliv.myths.common.Tuple;
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
import com.kaliv.myths.persistence.*;
import com.querydsl.core.BooleanBuilder;

@Service
public class MythServiceImpl implements MythService {

    private final MythRepository mythRepository;
    private final NationalityRepository nationalityRepository;
    private final MythCharacterRepository mythCharacterRepository;
    private final StatueRepository statueRepository;
    private final PaintingRepository paintingRepository;
    private final MusicRepository musicRepository;
    private final PoemRepository poemRepository;
    private final MythMapper mapper;

    public MythServiceImpl(MythRepository mythRepository,
                           NationalityRepository nationalityRepository,
                           MythCharacterRepository mythCharacterRepository,
                           StatueRepository statueRepository,
                           PaintingRepository paintingRepository,
                           MusicRepository musicRepository,
                           PoemRepository poemRepository,
                           MythMapper mapper) {
        this.mythRepository = mythRepository;
        this.nationalityRepository = nationalityRepository;
        this.mythCharacterRepository = mythCharacterRepository;
        this.statueRepository = statueRepository;
        this.paintingRepository = paintingRepository;
        this.musicRepository = musicRepository;
        this.poemRepository = poemRepository;
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

        Quadruple<List<Statue>, List<Painting>, List<Music>, List<Poem>> artworks = this.getValidArtworks(dto);
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

        handleCollectionsToUpdate(dto, mythInDb);
        mythRepository.save(mythInDb);
        return mapper.mythToDto(mythInDb);
    }

    @Override
    public void deleteMyth(long id) {
        Myth myth = mythRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MYTH, Fields.ID, id));
        mythRepository.delete(myth);
    }

    private Quadruple<List<Statue>, List<Painting>, List<Music>, List<Poem>> getValidArtworks(CreateMythDto dto) {
        List<Long> statueIds = new ArrayList<>(dto.getStatueIds());
        List<Statue> statues = statueRepository.findAllById(statueIds);
        if (statues.size() != statueIds.size()) {
            throw new ResourceListNotFoundException(Sources.STATUES, Fields.IDS);
        }
        List<Long> paintingIds = new ArrayList<>(dto.getPaintingIds());
        List<Painting> paintings = paintingRepository.findAllById(paintingIds);
        if (paintings.size() != paintingIds.size()) {
            throw new ResourceListNotFoundException(Sources.PAINTINGS, Fields.IDS);
        }
        List<Long> musicIds = new ArrayList<>(dto.getMusicIds());
        List<Music> music = musicRepository.findAllById(musicIds);
        if (music.size() != musicIds.size()) {
            throw new ResourceListNotFoundException(Sources.MUSIC, Fields.IDS);
        }
        List<Long> poemIds = new ArrayList<>(dto.getPoemIds());
        List<Poem> poems = poemRepository.findAllById(poemIds);
        if (poems.size() != poemIds.size()) {
            throw new ResourceListNotFoundException(Sources.POEMS, Fields.IDS);
        }
        return new Quadruple<>(statues, paintings, music, poems);
    }

    private void handleCollectionsToUpdate(UpdateMythDto dto, Myth mythInDb) {
        Tuple<List<MythCharacter>, List<MythCharacter>> mythCharactersToUpdate = this.getValidMythCharacters(dto, mythInDb);
        List<MythCharacter> mythCharactersToAdd = mythCharactersToUpdate.getFirst();
        List<MythCharacter> mythCharactersToRemove = mythCharactersToUpdate.getSecond();
        mythInDb.getMythCharacters().addAll(new HashSet<>(mythCharactersToAdd));
        mythInDb.getMythCharacters().removeAll(new HashSet<>(mythCharactersToRemove));

        Tuple<List<Statue>, List<Statue>> statuesToUpdate = this.getValidStatues(dto, mythInDb);
        List<Statue> statuesToAdd = statuesToUpdate.getFirst();
        List<Statue> statuesToRemove = statuesToUpdate.getSecond();
        mythInDb.getStatues().addAll(new HashSet<>(statuesToAdd));
        mythInDb.getStatues().removeAll(new HashSet<>(statuesToRemove));

        Tuple<List<Painting>, List<Painting>> paintingsToUpdate = this.getValidPaintings(dto, mythInDb);
        List<Painting> paintingsToAdd = paintingsToUpdate.getFirst();
        List<Painting> paintingsToRemove = paintingsToUpdate.getSecond();
        mythInDb.getPaintings().addAll(new HashSet<>(paintingsToAdd));
        mythInDb.getPaintings().removeAll(new HashSet<>(paintingsToRemove));

        Tuple<List<Music>, List<Music>> musicToUpdate = this.getValidMusic(dto, mythInDb);
        List<Music> musicToAdd = musicToUpdate.getFirst();
        List<Music> musicToRemove = musicToUpdate.getSecond();
        mythInDb.getMusic().addAll(new HashSet<>(musicToAdd));
        mythInDb.getMusic().removeAll(new HashSet<>(musicToRemove));

        Tuple<List<Poem>, List<Poem>> poemsToUpdate = this.getValidPoems(dto, mythInDb);
        List<Poem> poemsToAdd = poemsToUpdate.getFirst();
        List<Poem> poemsToRemove = poemsToUpdate.getSecond();
        mythInDb.getPoems().addAll(new HashSet<>(poemsToAdd));
        mythInDb.getPoems().removeAll(new HashSet<>(poemsToRemove));
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

    private Tuple<List<Statue>, List<Statue>> getValidStatues(UpdateMythDto dto, Myth mythInDb) {
        List<Long> statuesToAddIds = new ArrayList<>(dto.getStatuesToAdd());
        List<Long> statuesToRemoveIds = new ArrayList<>(dto.getStatuesToRemove());
        //check if user tries to add and remove same statue
        if (!Collections.disjoint(statuesToAddIds, statuesToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_STATUES, Sources.REMOVE_STATUES);
        }
        //check if user tries to add statue that is already in the list
        if (mythInDb.getStatues().stream()
                .map(BaseEntity::getId)
                .anyMatch(statuesToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.STATUE);
        }
        //check if user tries to remove statue that is not in the list
        if (!mythInDb.getStatues().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet())
                .containsAll(statuesToRemoveIds)) {
            throw new ResourceNotFoundException(Sources.STATUE);
        }

        List<Statue> statuesToAdd = statueRepository.findAllById(statuesToAddIds);
        List<Statue> statuesToRemove = statueRepository.findAllById(statuesToRemoveIds);
        if (statuesToAddIds.size() != statuesToAdd.size()
                || statuesToRemoveIds.size() != statuesToRemove.size()) {
            throw new ResourceListNotFoundException(Sources.STATUES, Fields.IDS);
        }
        return new Tuple<>(statuesToAdd, statuesToRemove);
    }

    private Tuple<List<Painting>, List<Painting>> getValidPaintings(UpdateMythDto dto, Myth mythInDb) {
        List<Long> paintingsToAddIds = new ArrayList<>(dto.getPaintingsToAdd());
        List<Long> paintingsToRemoveIds = new ArrayList<>(dto.getPaintingsToRemove());
        //check if user tries to add and remove same painting
        if (!Collections.disjoint(paintingsToAddIds, paintingsToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_PAINTINGS, Sources.REMOVE_PAINTINGS);
        }
        //check if user tries to add painting that is already in the list
        if (mythInDb.getPaintings().stream()
                .map(BaseEntity::getId)
                .anyMatch(paintingsToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.PAINTING);
        }
        //check if user tries to remove painting that is not in the list
        if (!mythInDb.getPaintings().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet())
                .containsAll(paintingsToRemoveIds)) {
            throw new ResourceNotFoundException(Sources.PAINTING);
        }

        List<Painting> paintingsToAdd = paintingRepository.findAllById(paintingsToAddIds);
        List<Painting> paintingsToRemove = paintingRepository.findAllById(paintingsToRemoveIds);
        if (paintingsToAddIds.size() != paintingsToAdd.size()
                || paintingsToRemoveIds.size() != paintingsToRemove.size()) {
            throw new ResourceListNotFoundException(Sources.PAINTING, Fields.IDS);
        }
        return new Tuple<>(paintingsToAdd, paintingsToRemove);
    }

    private Tuple<List<Music>, List<Music>> getValidMusic(UpdateMythDto dto, Myth mythInDb) {
        List<Long> musicToAddIds = new ArrayList<>(dto.getMusicToAdd());
        List<Long> musicToRemoveIds = new ArrayList<>(dto.getMusicToRemove());
        //check if user tries to add and remove same painting
        if (!Collections.disjoint(musicToAddIds, musicToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_MUSIC, Sources.REMOVE_MUSIC);
        }
        //check if user tries to add painting that is already in the list
        if (mythInDb.getMusic().stream()
                .map(BaseEntity::getId)
                .anyMatch(musicToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.MUSIC);
        }
        //check if user tries to remove painting that is not in the list
        if (!mythInDb.getMusic().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet())
                .containsAll(musicToRemoveIds)) {
            throw new ResourceNotFoundException(Sources.MUSIC);
        }

        List<Music> musicToAdd = musicRepository.findAllById(musicToAddIds);
        List<Music> musicToRemove = musicRepository.findAllById(musicToRemoveIds);
        if (musicToAddIds.size() != musicToAdd.size()
                || musicToRemoveIds.size() != musicToRemove.size()) {
            throw new ResourceListNotFoundException(Sources.MUSIC, Fields.IDS);
        }
        return new Tuple<>(musicToAdd, musicToRemove);
    }

    private Tuple<List<Poem>, List<Poem>> getValidPoems(UpdateMythDto dto, Myth mythInDb) {
        List<Long> poemsToAddIds = new ArrayList<>(dto.getPoemsToAdd());
        List<Long> poemsToRemoveIds = new ArrayList<>(dto.getPoemsToRemove());
        //check if user tries to add and remove same painting
        if (!Collections.disjoint(poemsToAddIds, poemsToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_POEMS, Sources.REMOVE_POEMS);
        }
        //check if user tries to add painting that is already in the list
        if (mythInDb.getPoems().stream()
                .map(BaseEntity::getId)
                .anyMatch(poemsToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.POEM);
        }
        //check if user tries to remove painting that is not in the list
        if (!mythInDb.getPoems().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet())
                .containsAll(poemsToRemoveIds)) {
            throw new ResourceNotFoundException(Sources.POEM);
        }

        List<Poem> poemsToAdd = poemRepository.findAllById(poemsToAddIds);
        List<Poem> poemsToRemove = poemRepository.findAllById(poemsToRemoveIds);
        if (poemsToAddIds.size() != poemsToAdd.size()
                || poemsToRemoveIds.size() != poemsToRemove.size()) {
            throw new ResourceListNotFoundException(Sources.POEMS, Fields.IDS);
        }
        return new Tuple<>(poemsToAdd, poemsToRemove);
    }
}
