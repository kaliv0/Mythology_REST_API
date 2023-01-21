package com.kaliv.myths.service.painting;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.common.utils.Tuple;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.paintingDtos.CreatePaintingDto;
import com.kaliv.myths.dto.paintingDtos.PaintingDto;
import com.kaliv.myths.dto.paintingDtos.PaintingResponseDto;
import com.kaliv.myths.dto.paintingDtos.UpdatePaintingDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Myth;
import com.kaliv.myths.entity.MythCharacter;
import com.kaliv.myths.entity.artefacts.Author;
import com.kaliv.myths.entity.artefacts.Museum;
import com.kaliv.myths.entity.artefacts.Painting;
import com.kaliv.myths.entity.artefacts.images.PaintingImage;
import com.kaliv.myths.exception.alreadyExists.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.invalidInput.DuplicateEntriesException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.PaintingMapper;
import com.kaliv.myths.persistence.*;

@Service
public class PaintingServiceImpl implements PaintingService {

    private final PaintingRepository paintingRepository;
    private final AuthorRepository authorRepository;
    private final MythRepository mythRepository;
    private final MuseumRepository museumRepository;
    private final MythCharacterRepository mythCharacterRepository;
    private final PaintingImageRepository paintingImageRepository;
    private final PaintingMapper mapper;

    public PaintingServiceImpl(PaintingRepository paintingRepository, AuthorRepository authorRepository, MythRepository mythRepository, MuseumRepository museumRepository, MythCharacterRepository mythCharacterRepository, PaintingImageRepository paintingImageRepository, PaintingMapper mapper) {
        this.paintingRepository = paintingRepository;
        this.authorRepository = authorRepository;
        this.mythRepository = mythRepository;
        this.museumRepository = museumRepository;
        this.mythCharacterRepository = mythCharacterRepository;
        this.paintingImageRepository = paintingImageRepository;
        this.mapper = mapper;
    }

    @Override
    public List<PaintingResponseDto> getAllPaintings() {
        return paintingRepository.findAll().stream().map(mapper::paintingToResponseDto).collect(Collectors.toList());
    }

    @Override
    public PaintingResponseDto getPaintingById(long id) {
        Painting paintingInDb = paintingRepository.findById(id).orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.PAINTING, Fields.ID, id));
        return mapper.paintingToResponseDto(paintingInDb);
    }

    @Override
    public PaintingDto createPainting(CreatePaintingDto dto) {
        String name = dto.getName();
        if (paintingRepository.existsByName(name)) {
            throw new ResourceWithGivenValuesExistsException(Sources.PAINTING, Fields.NAME, name);
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

        List<Long> paintingImageIds = new ArrayList<>(dto.getPaintingImageIds());
        List<PaintingImage> paintingImages = paintingImageRepository.findAllById(paintingImageIds);
        if (paintingImages.size() != paintingImageIds.size()) {
            throw new ResourceListNotFoundException(Sources.IMAGES, Fields.IDS);
        }

        Painting painting = mapper.dtoToPainting(dto);
        Painting savedPainting = paintingRepository.save(painting);

        /*TODO: check if works without inverse properties   */

//        mythCharacters.forEach(a -> a.setPainting(savedPainting));
//        mythCharacterRepository.saveAll(mythCharacters);
        //add paintingImages.forEach(...)

        return mapper.paintingToDto(savedPainting);
    }

    @Override
    public PaintingDto updatePainting(long id, UpdatePaintingDto dto) {
        Painting paintingInDb = paintingRepository.findById(id).orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.PAINTING, Fields.ID, id));

        if (Optional.ofNullable(dto.getName()).isPresent()) {
            String newName = dto.getName();
            if (!newName.equals(paintingInDb.getName()) && paintingRepository.existsByName(newName)) {
                throw new ResourceWithGivenValuesExistsException(Sources.PAINTING, Fields.NAME, newName);
            }
            paintingInDb.setName(dto.getName());
        }

        if (Optional.ofNullable(dto.getAuthorId()).isPresent()) {
            long authorId = dto.getAuthorId();
            Author authorInDb = authorRepository.findById(authorId).orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.AUTHOR, Fields.ID, authorId));
            paintingInDb.setAuthor(authorInDb);
        }

        if (Optional.ofNullable(dto.getMythId()).isPresent()) {
            long mythId = dto.getMythId();
            Myth mythInDb = mythRepository.findById(mythId).orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MYTH, Fields.ID, mythId));
            paintingInDb.setMyth(mythInDb);
        }

        if (Optional.ofNullable(dto.getMuseumId()).isPresent()) {
            long museumId = dto.getMuseumId();
            Museum museumInDb = museumRepository.findById(museumId).orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MUSEUM, Fields.ID, museumId));
            paintingInDb.setMuseum(museumInDb);
        }

        Tuple<List<MythCharacter>, List<MythCharacter>> mythCharactersToUpdate = this.getValidMythCharacters(dto, paintingInDb);
        List<MythCharacter> mythCharactersToAdd = mythCharactersToUpdate.getFirst();
        List<MythCharacter> mythCharactersToRemove = mythCharactersToUpdate.getSecond();
        paintingInDb.getMythCharacters().addAll(new HashSet<>(mythCharactersToAdd));
        paintingInDb.getMythCharacters().removeAll(new HashSet<>(mythCharactersToRemove));

        Tuple<List<PaintingImage>, List<PaintingImage>> paintingImagesToUpdate = this.getValidPaintingImages(dto, paintingInDb);
        List<PaintingImage> paintingImagesToAdd = paintingImagesToUpdate.getFirst();
        List<PaintingImage> paintingImagesToRemove = paintingImagesToUpdate.getSecond();
        paintingInDb.getPaintingImages().addAll(new HashSet<>(paintingImagesToAdd));
        paintingInDb.getPaintingImages().removeAll(new HashSet<>(paintingImagesToRemove));

        paintingRepository.save(paintingInDb);

        /*TODO: check if works without inverse properties   */

//        mythCharactersToAdd.forEach(a -> a.setPainting(paintingInDb));
//        mythCharacterRepository.saveAll(mythCharactersToAdd);
//        mythCharactersToRemove.forEach(a -> a.setPainting(null));
//        mythCharacterRepository.saveAll(mythCharactersToRemove);
//        handle paintingIamges.forEach(...)
        return mapper.paintingToDto(paintingInDb);
    }

    @Override
    public void deletePainting(long id) {
        Painting paintingInDb = paintingRepository.findById(id).orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.PAINTING, Fields.ID, id));
        paintingRepository.delete(paintingInDb);
    }

    private Tuple<List<MythCharacter>, List<MythCharacter>> getValidMythCharacters(UpdatePaintingDto dto, Painting paintingInDb) {
        List<Long> mythCharactersToAddIds = new ArrayList<>(dto.getMythCharactersToAdd());
        List<Long> mythCharactersToRemoveIds = new ArrayList<>(dto.getMythCharactersToRemove());
        //check if user tries to add and remove same mythCharacter
        if (!Collections.disjoint(mythCharactersToAddIds, mythCharactersToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_CHARACTERS, Sources.REMOVE_CHARACTERS);
        }
        //check if user tries to add mythCharacter that is already in the list
        if (paintingInDb.getMythCharacters().stream().map(BaseEntity::getId).anyMatch(mythCharactersToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.CHARACTER);
        }
        //check if user tries to remove mythCharacter that is not in the list
        if (!paintingInDb.getMythCharacters().stream().map(BaseEntity::getId).collect(Collectors.toSet()).containsAll(mythCharactersToRemoveIds)) {
            throw new ResourceNotFoundException(Sources.CHARACTER);
        }

        List<MythCharacter> mythCharactersToAdd = mythCharacterRepository.findAllById(mythCharactersToAddIds);
        List<MythCharacter> mythCharactersToRemove = mythCharacterRepository.findAllById(mythCharactersToRemoveIds);
        if (mythCharactersToAddIds.size() != mythCharactersToAdd.size() || mythCharactersToRemoveIds.size() != mythCharactersToRemove.size()) {
            throw new ResourceListNotFoundException(Sources.CHARACTERS, Fields.IDS);
        }
        return new Tuple<>(mythCharactersToAdd, mythCharactersToRemove);
    }

    private Tuple<List<PaintingImage>, List<PaintingImage>> getValidPaintingImages(UpdatePaintingDto dto, Painting paintingInDb) {
        List<Long> paintingImagesToAddIds = new ArrayList<>(dto.getPaintingImagesToAdd());
        List<Long> paintingImagesToRemoveIds = new ArrayList<>(dto.getPaintingImagesToRemove());
        //check if user tries to add and remove same paintingImage
        if (!Collections.disjoint(paintingImagesToAddIds, paintingImagesToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_IMAGES, Sources.REMOVE_IMAGES);
        }
        //check if user tries to add paintingImage that is already in the list
        if (paintingInDb.getPaintingImages().stream().map(BaseEntity::getId).anyMatch(paintingImagesToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.IMAGE);
        }
        //check if user tries to remove paintingImage that is not in the list
        if (!paintingInDb.getPaintingImages().stream().map(BaseEntity::getId).collect(Collectors.toSet()).containsAll(paintingImagesToRemoveIds)) {
            throw new ResourceNotFoundException(Sources.IMAGE);
        }

        List<PaintingImage> paintingImagesToAdd = paintingImageRepository.findAllById(paintingImagesToAddIds);
        List<PaintingImage> paintingImagesToRemove = paintingImageRepository.findAllById(paintingImagesToRemoveIds);
        if (paintingImagesToAddIds.size() != paintingImagesToAdd.size() || paintingImagesToRemoveIds.size() != paintingImagesToRemove.size()) {
            throw new ResourceListNotFoundException(Sources.IMAGES, Fields.IDS);
        }
        return new Tuple<>(paintingImagesToAdd, paintingImagesToRemove);
    }
}