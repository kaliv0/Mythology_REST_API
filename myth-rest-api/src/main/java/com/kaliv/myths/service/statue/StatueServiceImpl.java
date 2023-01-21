package com.kaliv.myths.service.statue;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.common.utils.Tuple;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.statueDtos.CreateStatueDto;
import com.kaliv.myths.dto.statueDtos.StatueDto;
import com.kaliv.myths.dto.statueDtos.StatueResponseDto;
import com.kaliv.myths.dto.statueDtos.UpdateStatueDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Myth;
import com.kaliv.myths.entity.MythCharacter;
import com.kaliv.myths.entity.artefacts.Author;
import com.kaliv.myths.entity.artefacts.Museum;
import com.kaliv.myths.entity.artefacts.Statue;
import com.kaliv.myths.entity.artefacts.images.StatueImage;
import com.kaliv.myths.exception.alreadyExists.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.invalidInput.DuplicateEntriesException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.StatueMapper;
import com.kaliv.myths.persistence.*;

@Service
public class StatueServiceImpl implements StatueService {

    private final StatueRepository statueRepository;
    private final AuthorRepository authorRepository;
    private final MythRepository mythRepository;
    private final MuseumRepository museumRepository;
    private final MythCharacterRepository mythCharacterRepository;
    private final StatueImageRepository statueImageRepository;
    private final StatueMapper mapper;

    public StatueServiceImpl(StatueRepository statueRepository,
                             AuthorRepository authorRepository,
                             MythRepository mythRepository,
                             MuseumRepository museumRepository,
                             MythCharacterRepository mythCharacterRepository,
                             StatueImageRepository statueImageRepository,
                             StatueMapper mapper) {
        this.statueRepository = statueRepository;
        this.authorRepository = authorRepository;
        this.mythRepository = mythRepository;
        this.museumRepository = museumRepository;
        this.mythCharacterRepository = mythCharacterRepository;
        this.statueImageRepository = statueImageRepository;
        this.mapper = mapper;
    }

    @Override
    public List<StatueResponseDto> getAllStatues() {
        return statueRepository.findAll().stream()
                .map(mapper::statueToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public StatueResponseDto getStatueById(long id) {
        Statue statueInDb = statueRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.STATUE, Fields.ID, id));
        return mapper.statueToResponseDto(statueInDb);
    }

    @Override
    public StatueDto createStatue(CreateStatueDto dto) {
        String name = dto.getName();
        if (statueRepository.existsByName(name)) {
            throw new ResourceWithGivenValuesExistsException(Sources.STATUE, Fields.NAME, name);
        }

        Long authorId = dto.getAuthorId();
        if (authorId != null && !authorRepository.existsById(authorId)) {
            throw new ResourceWithGivenValuesNotFoundException(Sources.AUTHOR, Fields.ID, authorId);
        }

        Long mythId = dto.getMythId();
        if (mythId != null && !mythRepository.existsById(mythId)) {
            throw new ResourceWithGivenValuesNotFoundException(Sources.MYTH, Fields.ID, mythId);
        }

        Long museumId = dto.getMuseumId();
        if (museumId != null && !museumRepository.existsById(museumId)) {
            throw new ResourceWithGivenValuesNotFoundException(Sources.MUSEUM, Fields.ID, museumId);
        }

        List<Long> mythCharacterIds = new ArrayList<>(dto.getMythCharacterIds());
        List<MythCharacter> mythCharacters = mythCharacterRepository.findAllById(mythCharacterIds);
        if (mythCharacters.size() != mythCharacterIds.size()) {
            throw new ResourceListNotFoundException(Sources.CHARACTERS, Fields.IDS);
        }

        List<Long> statueImageIds = new ArrayList<>(dto.getStatueImageIds());
        List<StatueImage> statueImages = statueImageRepository.findAllById(statueImageIds);
        if (statueImages.size() != statueImageIds.size()) {
            throw new ResourceListNotFoundException(Sources.IMAGES, Fields.IDS);
        }

        Statue statue = mapper.dtoToStatue(dto);
        Statue savedStatue = statueRepository.save(statue);

        /*TODO: check if works without inverse properties   */

//        mythCharacters.forEach(a -> a.setStatue(savedStatue));
//        mythCharacterRepository.saveAll(mythCharacters);
        //add statueImages.forEach(...)

        return mapper.statueToDto(savedStatue);
    }

    @Override
    public StatueDto updateStatue(long id, UpdateStatueDto dto) {
        Statue statueInDb = statueRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.STATUE, Fields.ID, id));

        if (Optional.ofNullable(dto.getName()).isPresent()) {
            String newName = dto.getName();
            if (!newName.equals(statueInDb.getName()) && statueRepository.existsByName(newName)) {
                throw new ResourceWithGivenValuesExistsException(Sources.STATUE, Fields.NAME, newName);
            }
            statueInDb.setName(dto.getName());
        }

        if (Optional.ofNullable(dto.getAuthorId()).isPresent()) {
            long authorId = dto.getAuthorId();
            Author authorInDb = authorRepository.findById(authorId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.AUTHOR, Fields.ID, authorId));
            statueInDb.setAuthor(authorInDb);
        }

        if (Optional.ofNullable(dto.getMythId()).isPresent()) {
            long mythId = dto.getMythId();
            Myth mythInDb = mythRepository.findById(mythId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MYTH, Fields.ID, mythId));
            statueInDb.setMyth(mythInDb);
        }

        if (Optional.ofNullable(dto.getMuseumId()).isPresent()) {
            long museumId = dto.getMuseumId();
            Museum museumInDb = museumRepository.findById(museumId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MUSEUM, Fields.ID, museumId));
            statueInDb.setMuseum(museumInDb);
        }

        Tuple<List<MythCharacter>, List<MythCharacter>> mythCharactersToUpdate = this.getValidMythCharacters(dto, statueInDb);
        List<MythCharacter> mythCharactersToAdd = mythCharactersToUpdate.getFirst();
        List<MythCharacter> mythCharactersToRemove = mythCharactersToUpdate.getSecond();
        statueInDb.getMythCharacters().addAll(new HashSet<>(mythCharactersToAdd));
        statueInDb.getMythCharacters().removeAll(new HashSet<>(mythCharactersToRemove));

        Tuple<List<StatueImage>, List<StatueImage>> statueImagesToUpdate = this.getValidStatueImages(dto, statueInDb);
        List<StatueImage> statueImagesToAdd = statueImagesToUpdate.getFirst();
        List<StatueImage> statueImagesToRemove = statueImagesToUpdate.getSecond();
        statueInDb.getStatueImages().addAll(new HashSet<>(statueImagesToAdd));
        statueInDb.getStatueImages().removeAll(new HashSet<>(statueImagesToRemove));

        statueRepository.save(statueInDb);

        /*TODO: check if works without inverse properties   */

//        mythCharactersToAdd.forEach(a -> a.setStatue(statueInDb));
//        mythCharacterRepository.saveAll(mythCharactersToAdd);
//        mythCharactersToRemove.forEach(a -> a.setStatue(null));
//        mythCharacterRepository.saveAll(mythCharactersToRemove);
//        handle statueIamges.forEach(...)
        return mapper.statueToDto(statueInDb);
    }

    @Override
    public void deleteStatue(long id) {
        Statue statueInDb = statueRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.STATUE, Fields.ID, id));
        statueRepository.delete(statueInDb);
    }

    private Tuple<List<MythCharacter>, List<MythCharacter>> getValidMythCharacters(UpdateStatueDto dto, Statue statueInDb) {
        List<Long> mythCharactersToAddIds = new ArrayList<>(dto.getMythCharactersToAdd());
        List<Long> mythCharactersToRemoveIds = new ArrayList<>(dto.getMythCharactersToRemove());
        //check if user tries to add and remove same mythCharacter
        if (!Collections.disjoint(mythCharactersToAddIds, mythCharactersToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_CHARACTERS, Sources.REMOVE_CHARACTERS);
        }
        //check if user tries to add mythCharacter that is already in the list
        if (statueInDb.getMythCharacters().stream()
                .map(BaseEntity::getId)
                .anyMatch(mythCharactersToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.CHARACTER);
        }
        //check if user tries to remove mythCharacter that is not in the list
        if (!statueInDb.getMythCharacters().stream()
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

    private Tuple<List<StatueImage>, List<StatueImage>> getValidStatueImages(UpdateStatueDto dto, Statue statueInDb) {
        List<Long> statueImagesToAddIds = new ArrayList<>(dto.getStatueImagesToAdd());
        List<Long> statueImagesToRemoveIds = new ArrayList<>(dto.getStatueImagesToRemove());
        //check if user tries to add and remove same statueImage
        if (!Collections.disjoint(statueImagesToAddIds, statueImagesToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_IMAGES, Sources.REMOVE_IMAGES);
        }
        //check if user tries to add statueImage that is already in the list
        if (statueInDb.getStatueImages().stream()
                .map(BaseEntity::getId)
                .anyMatch(statueImagesToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.IMAGE);
        }
        //check if user tries to remove statueImage that is not in the list
        if (!statueInDb.getStatueImages().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet())
                .containsAll(statueImagesToRemoveIds)) {
            throw new ResourceNotFoundException(Sources.IMAGE);
        }

        List<StatueImage> statueImagesToAdd = statueImageRepository.findAllById(statueImagesToAddIds);
        List<StatueImage> statueImagesToRemove = statueImageRepository.findAllById(statueImagesToRemoveIds);
        if (statueImagesToAddIds.size() != statueImagesToAdd.size()
                || statueImagesToRemoveIds.size() != statueImagesToRemove.size()) {
            throw new ResourceListNotFoundException(Sources.IMAGES, Fields.IDS);
        }
        return new Tuple<>(statueImagesToAdd, statueImagesToRemove);
    }
}