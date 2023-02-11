package com.kaliv.myths.service.music;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kaliv.myths.common.Tuple;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.musicDtos.*;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Myth;
import com.kaliv.myths.entity.MythCharacter;
import com.kaliv.myths.entity.artefacts.Author;
import com.kaliv.myths.entity.artefacts.Music;
import com.kaliv.myths.entity.artefacts.QMusic;
import com.kaliv.myths.exception.alreadyExists.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.invalidInput.DuplicateEntriesException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.MusicMapper;
import com.kaliv.myths.persistence.AuthorRepository;
import com.kaliv.myths.persistence.MusicRepository;
import com.kaliv.myths.persistence.MythCharacterRepository;
import com.kaliv.myths.persistence.MythRepository;
import com.querydsl.core.BooleanBuilder;

@Service
public class MusicServiceImpl implements MusicService {
    private final MusicRepository musicRepository;
    private final AuthorRepository authorRepository;
    private final MythRepository mythRepository;
    private final MythCharacterRepository mythCharacterRepository;
    private final MusicMapper mapper;

    @Autowired
    public MusicServiceImpl(MusicRepository musicRepository,
                            AuthorRepository authorRepository,
                            MythRepository mythRepository,
                            MythCharacterRepository mythCharacterRepository,
                            MusicMapper mapper) {
        this.musicRepository = musicRepository;
        this.authorRepository = authorRepository;
        this.mythRepository = mythRepository;
        this.mythCharacterRepository = mythCharacterRepository;
        this.mapper = mapper;
    }

    @Override
    public PaginatedMusicResponseDto getAllMusic(String authorName,
                                                 String mythName,
                                                 String characterName,
                                                 int pageNumber,
                                                 int pageSize,
                                                 String sortBy,
                                                 String sortOrder) {
        QMusic qMusic = QMusic.music;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (authorName != null) {
            booleanBuilder.and(qMusic.author.name.equalsIgnoreCase(authorName));
        }
        if (mythName != null) {
            booleanBuilder.and(qMusic.myth.name.equalsIgnoreCase(mythName));
        }
        if (characterName != null) {
            booleanBuilder.and(qMusic.mythCharacters.any().name.equalsIgnoreCase(characterName));
        }

        Sort sortCriteria = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortCriteria);
        Page<Music> music = musicRepository.findAll(booleanBuilder, pageable);

        List<MusicResponseDto> content = music
                .getContent().stream()
                .map(mapper::musicToResponseDto)
                .collect(Collectors.toList());

        PaginatedMusicResponseDto musicResponseDto = new PaginatedMusicResponseDto();
        musicResponseDto.setContent(content);
        musicResponseDto.setPageNumber(music.getNumber());
        musicResponseDto.setPageSize(music.getSize());
        musicResponseDto.setTotalElements(music.getTotalElements());
        musicResponseDto.setTotalPages(music.getTotalPages());
        musicResponseDto.setLast(music.isLast());

        return musicResponseDto;
    }

    @Override
    public MusicResponseDto getMusicById(long id) {
        Music musicInDb = musicRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MUSIC, Fields.ID, id));
        return mapper.musicToResponseDto(musicInDb);
    }

    @Override
    public MusicDto createMusic(CreateMusicDto dto) {
        String name = dto.getName();
        if (musicRepository.existsByName(name)) {
            throw new ResourceWithGivenValuesExistsException(Sources.MUSIC, Fields.NAME, name);
        }

        Long authorId = dto.getAuthorId();
        if (authorId != null && !authorRepository.existsById(authorId)) {
            throw new ResourceWithGivenValuesNotFoundException(Sources.AUTHOR, Fields.ID, authorId);
        }

        Long mythId = dto.getMythId();
        if (mythId != null && !mythRepository.existsById(mythId)) {
            throw new ResourceWithGivenValuesNotFoundException(Sources.MYTH, Fields.ID, mythId);
        }

        List<Long> mythCharacterIds = new ArrayList<>(dto.getMythCharacterIds());
        List<MythCharacter> mythCharacters = mythCharacterRepository.findAllById(mythCharacterIds);
        if (mythCharacters.size() != mythCharacterIds.size()) {
            throw new ResourceListNotFoundException(Sources.CHARACTERS, Fields.IDS);
        }

        Music music = mapper.dtoToMusic(dto);
        music.setMythCharacters(new HashSet<>(mythCharacters));
        Music savedMusic = musicRepository.save(music);
        return mapper.musicToDto(savedMusic);
    }

    @Override
    public MusicDto updateMusic(long id, UpdateMusicDto dto) {
        Music musicInDb = musicRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MUSIC, Fields.ID, id));

        if (Optional.ofNullable(dto.getName()).isPresent()) {
            String newName = dto.getName();
            if (!newName.equals(musicInDb.getName()) && musicRepository.existsByName(newName)) {
                throw new ResourceWithGivenValuesExistsException(Sources.MUSIC, Fields.NAME, newName);
            }
            musicInDb.setName(dto.getName());
        }

        if (Optional.ofNullable(dto.getAuthorId()).isPresent()) {
            long authorId = dto.getAuthorId();
            Author authorInDb = authorRepository.findById(authorId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.AUTHOR, Fields.ID, authorId));
            musicInDb.setAuthor(authorInDb);
        }

        if (Optional.ofNullable(dto.getMythId()).isPresent()) {
            long mythId = dto.getMythId();
            Myth mythInDb = mythRepository.findById(mythId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MYTH, Fields.ID, mythId));
            musicInDb.setMyth(mythInDb);
        }

        Optional.ofNullable(dto.getRecordingUrl()).ifPresent(musicInDb::setRecordingUrl);

        this.handleCollectionsToUpdate(dto, musicInDb);
        musicRepository.save(musicInDb);
        return mapper.musicToDto(musicInDb);
    }

    @Override
    public void deleteMusic(long id) {
        Music musicInDb = musicRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MUSIC, Fields.ID, id));
        musicRepository.delete(musicInDb);
    }

    private void handleCollectionsToUpdate(UpdateMusicDto dto, Music musicInDb) {
        Tuple<List<MythCharacter>, List<MythCharacter>> mythCharactersToUpdate = this.getValidMythCharacters(dto, musicInDb);
        List<MythCharacter> mythCharactersToAdd = mythCharactersToUpdate.getFirst();
        List<MythCharacter> mythCharactersToRemove = mythCharactersToUpdate.getSecond();
        musicInDb.getMythCharacters().addAll(new HashSet<>(mythCharactersToAdd));
        musicInDb.getMythCharacters().removeAll(new HashSet<>(mythCharactersToRemove));

        mythCharactersToAdd.forEach(a -> a.getMusic().add(musicInDb));
        mythCharacterRepository.saveAll(mythCharactersToAdd);
        mythCharactersToRemove.forEach(a -> a.getMusic().remove(musicInDb));
        mythCharacterRepository.saveAll(mythCharactersToRemove);
    }

    private Tuple<List<MythCharacter>, List<MythCharacter>> getValidMythCharacters(UpdateMusicDto dto, Music musicInDb) {
        List<Long> mythCharactersToAddIds = new ArrayList<>(dto.getMythCharactersToAdd());
        List<Long> mythCharactersToRemoveIds = new ArrayList<>(dto.getMythCharactersToRemove());
        //check if user tries to add and remove same mythCharacter
        if (!Collections.disjoint(mythCharactersToAddIds, mythCharactersToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_CHARACTERS, Sources.REMOVE_CHARACTERS);
        }
        //check if user tries to add mythCharacter that is already in the list
        if (musicInDb.getMythCharacters().stream()
                .map(BaseEntity::getId)
                .anyMatch(mythCharactersToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.CHARACTER);
        }
        //check if user tries to remove mythCharacter that is not in the list
        if (!musicInDb.getMythCharacters().stream()
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
