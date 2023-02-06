package com.kaliv.myths.service.painting;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.kaliv.myths.common.container.Tuple;
import com.kaliv.myths.common.image.ImageResizeHandler;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.paintingDtos.*;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Myth;
import com.kaliv.myths.entity.MythCharacter;
import com.kaliv.myths.entity.artefacts.Author;
import com.kaliv.myths.entity.artefacts.Museum;
import com.kaliv.myths.entity.artefacts.Painting;
import com.kaliv.myths.entity.artefacts.QPainting;
import com.kaliv.myths.entity.artefacts.images.PaintingImage;
import com.kaliv.myths.entity.artefacts.images.QSmallPaintingImage;
import com.kaliv.myths.entity.artefacts.images.SmallPaintingImage;
import com.kaliv.myths.exception.alreadyExists.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.invalidInput.DuplicateEntriesException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.PaintingMapper;
import com.kaliv.myths.persistence.*;
import com.querydsl.core.BooleanBuilder;

@Service
public class PaintingServiceImpl implements PaintingService {

    private final PaintingRepository paintingRepository;
    private final AuthorRepository authorRepository;
    private final MythRepository mythRepository;
    private final MuseumRepository museumRepository;
    private final MythCharacterRepository mythCharacterRepository;
    private final PaintingImageRepository paintingImageRepository;
    private final SmallPaintingImageRepository smallPaintingImageRepository;
    private final PaintingMapper mapper;

    public PaintingServiceImpl(PaintingRepository paintingRepository,
                               AuthorRepository authorRepository,
                               MythRepository mythRepository,
                               MuseumRepository museumRepository,
                               MythCharacterRepository mythCharacterRepository,
                               PaintingImageRepository paintingImageRepository,
                               SmallPaintingImageRepository smallPaintingImageRepository,
                               PaintingMapper mapper) {
        this.paintingRepository = paintingRepository;
        this.authorRepository = authorRepository;
        this.mythRepository = mythRepository;
        this.museumRepository = museumRepository;
        this.mythCharacterRepository = mythCharacterRepository;
        this.paintingImageRepository = paintingImageRepository;
        this.smallPaintingImageRepository = smallPaintingImageRepository;
        this.mapper = mapper;
    }

    @Override
    public PaginatedPaintingResponseDto getAllPaintings(String authorName,
                                                        String mythName,
                                                        String museumName,
                                                        String characterName,
                                                        int pageNumber,
                                                        int pageSize,
                                                        String sortBy,
                                                        String sortOrder) {
        QPainting qPainting = QPainting.painting;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (authorName != null) {
            booleanBuilder.and(qPainting.author.name.equalsIgnoreCase(authorName));
        }
        if (mythName != null) {
            booleanBuilder.and(qPainting.myth.name.equalsIgnoreCase(mythName));
        }
        if (museumName != null) {
            booleanBuilder.and(qPainting.museum.name.equalsIgnoreCase(authorName));
        }
        if (characterName != null) {
            booleanBuilder.and(qPainting.mythCharacters.any().name.equalsIgnoreCase(characterName));
        }

        Sort sortCriteria = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortCriteria);
        Page<Painting> paintings = paintingRepository.findAll(booleanBuilder, pageable);

        List<PaintingResponseDto> content = paintings
                .getContent().stream()
                .map(mapper::paintingToResponseDto)
                .collect(Collectors.toList());

        PaginatedPaintingResponseDto paintingResponseDto = new PaginatedPaintingResponseDto();
        paintingResponseDto.setContent(content);
        paintingResponseDto.setPageNumber(paintings.getNumber());
        paintingResponseDto.setPageSize(paintings.getSize());
        paintingResponseDto.setTotalElements(paintings.getTotalElements());
        paintingResponseDto.setTotalPages(paintings.getTotalPages());
        paintingResponseDto.setLast(paintings.isLast());

        return paintingResponseDto;
    }

    @Override
    public PaintingResponseDto getPaintingById(long id) {
        Painting paintingInDb = paintingRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.PAINTING, Fields.ID, id));
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

        List<SmallPaintingImage> smallPaintingImages = this.getCorrespondingSmallPaintingImages(paintingImages);

        Painting painting = mapper.dtoToPainting(dto);
        painting.setMythCharacters(new HashSet<>(mythCharacters));
        painting.setPaintingImages(new HashSet<>(paintingImages));
        painting.setSmallPaintingImages(new HashSet<>(smallPaintingImages));
        Painting savedPainting = paintingRepository.save(painting);

        return mapper.paintingToDto(savedPainting);
    }

    @Override
    public PaintingDto updatePainting(long id, UpdatePaintingDto dto) {
        Painting paintingInDb = paintingRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.PAINTING, Fields.ID, id));

        if (Optional.ofNullable(dto.getName()).isPresent()) {
            String newName = dto.getName();
            if (!newName.equals(paintingInDb.getName()) && paintingRepository.existsByName(newName)) {
                throw new ResourceWithGivenValuesExistsException(Sources.PAINTING, Fields.NAME, newName);
            }
            paintingInDb.setName(dto.getName());
        }
        if (Optional.ofNullable(dto.getAuthorId()).isPresent()) {
            long authorId = dto.getAuthorId();
            Author authorInDb = authorRepository.findById(authorId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.AUTHOR, Fields.ID, authorId));
            paintingInDb.setAuthor(authorInDb);
        }
        if (Optional.ofNullable(dto.getMythId()).isPresent()) {
            long mythId = dto.getMythId();
            Myth mythInDb = mythRepository.findById(mythId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MYTH, Fields.ID, mythId));
            paintingInDb.setMyth(mythInDb);
        }
        if (Optional.ofNullable(dto.getMuseumId()).isPresent()) {
            long museumId = dto.getMuseumId();
            Museum museumInDb = museumRepository.findById(museumId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MUSEUM, Fields.ID, museumId));
            paintingInDb.setMuseum(museumInDb);
        }

        this.handleCollectionsToUpdate(dto, paintingInDb);
        paintingRepository.save(paintingInDb);
        return mapper.paintingToDto(paintingInDb);
    }

    @Override
    public void deletePainting(long id) {
        Painting paintingInDb = paintingRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.PAINTING, Fields.ID, id));
        paintingRepository.delete(paintingInDb);
    }

    private void handleCollectionsToUpdate(UpdatePaintingDto dto, Painting paintingInDb) {
        Tuple<List<MythCharacter>, List<MythCharacter>> mythCharactersToUpdate = this.getValidMythCharacters(dto, paintingInDb);
        List<MythCharacter> mythCharactersToAdd = mythCharactersToUpdate.getFirst();
        List<MythCharacter> mythCharactersToRemove = mythCharactersToUpdate.getSecond();
        paintingInDb.getMythCharacters().addAll(new HashSet<>(mythCharactersToAdd));
        paintingInDb.getMythCharacters().removeAll(new HashSet<>(mythCharactersToRemove));

        Tuple<List<PaintingImage>, List<PaintingImage>> paintingImagesToUpdate = this.getValidPaintingImages(dto, paintingInDb);
        List<PaintingImage> paintingImagesToAdd = paintingImagesToUpdate.getFirst();
        List<PaintingImage> paintingImagesToRemove = paintingImagesToUpdate.getSecond();
        List<SmallPaintingImage> smallPaintingImagesToAdd = getCorrespondingSmallPaintingImages(paintingImagesToAdd);
        List<SmallPaintingImage> smallPaintingImagesToRemove = getCorrespondingSmallPaintingImages(paintingImagesToRemove);

        paintingInDb.getPaintingImages().addAll(new HashSet<>(paintingImagesToAdd));
        paintingInDb.getPaintingImages().removeAll(new HashSet<>(paintingImagesToRemove));
        paintingInDb.getSmallPaintingImages().addAll(new HashSet<>(smallPaintingImagesToAdd));
        paintingInDb.getSmallPaintingImages().removeAll(new HashSet<>(smallPaintingImagesToRemove));

        mythCharactersToAdd.forEach(character -> character.getPaintings().add(paintingInDb));
        mythCharacterRepository.saveAll(mythCharactersToAdd);
        mythCharactersToRemove.forEach(character -> character.getPaintings().remove(paintingInDb));
        mythCharacterRepository.saveAll(mythCharactersToRemove);

        paintingImagesToAdd.forEach(image -> image.setPainting(paintingInDb));
        paintingImageRepository.saveAll(paintingImagesToAdd);
        paintingImagesToRemove.forEach(image -> image.setPainting(null));
        paintingImageRepository.saveAll(paintingImagesToRemove);

        smallPaintingImagesToAdd.forEach(image -> image.setPainting(paintingInDb));
        smallPaintingImageRepository.saveAll(smallPaintingImagesToAdd);
        smallPaintingImagesToRemove.forEach(image -> image.setPainting(null));
        smallPaintingImageRepository.saveAll(smallPaintingImagesToRemove);
    }

    private List<SmallPaintingImage> getCorrespondingSmallPaintingImages(List<PaintingImage> paintingImages) {
        Set<String> paintingImageNames = paintingImages.stream()
                .map(image -> ImageResizeHandler.prepareResizedFileName(image.getName()))
                .collect(Collectors.toSet());
        QSmallPaintingImage qSmallPaintingImage = QSmallPaintingImage.smallPaintingImage;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qSmallPaintingImage.name.in(paintingImageNames));
        List<SmallPaintingImage> smallPaintingImages = Lists.newArrayList(smallPaintingImageRepository.findAll(booleanBuilder));
        if (smallPaintingImages.size() != paintingImages.size()) {
            throw new ResourceListNotFoundException(Sources.SMALL_IMAGES, Fields.NAMES);
        }
        return smallPaintingImages;
    }

    private Tuple<List<MythCharacter>, List<MythCharacter>> getValidMythCharacters(UpdatePaintingDto dto, Painting paintingInDb) {
        List<Long> mythCharactersToAddIds = new ArrayList<>(dto.getMythCharactersToAdd());
        List<Long> mythCharactersToRemoveIds = new ArrayList<>(dto.getMythCharactersToRemove());
        //check if user tries to add and remove same mythCharacter
        if (!Collections.disjoint(mythCharactersToAddIds, mythCharactersToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_CHARACTERS, Sources.REMOVE_CHARACTERS);
        }
        //check if user tries to add mythCharacter that is already in the list
        if (paintingInDb.getMythCharacters().stream()
                .map(BaseEntity::getId)
                .anyMatch(mythCharactersToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.CHARACTER);
        }
        //check if user tries to remove mythCharacter that is not in the list
        if (!paintingInDb.getMythCharacters().stream()
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

    private Tuple<List<PaintingImage>, List<PaintingImage>> getValidPaintingImages(UpdatePaintingDto dto, Painting paintingInDb) {
        List<Long> paintingImagesToAddIds = new ArrayList<>(dto.getPaintingImagesToAdd());
        List<Long> paintingImagesToRemoveIds = new ArrayList<>(dto.getPaintingImagesToRemove());
        //check if user tries to add and remove same paintingImage
        if (!Collections.disjoint(paintingImagesToAddIds, paintingImagesToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_IMAGES, Sources.REMOVE_IMAGES);
        }
        //check if user tries to add paintingImage that is already in the list
        if (paintingInDb.getPaintingImages().stream()
                .map(BaseEntity::getId)
                .anyMatch(paintingImagesToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.IMAGE);
        }
        //check if user tries to remove paintingImage that is not in the list
        if (!paintingInDb.getPaintingImages().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet())
                .containsAll(paintingImagesToRemoveIds)) {
            throw new ResourceNotFoundException(Sources.IMAGE);
        }

        List<PaintingImage> paintingImagesToAdd = paintingImageRepository.findAllById(paintingImagesToAddIds);
        List<PaintingImage> paintingImagesToRemove = paintingImageRepository.findAllById(paintingImagesToRemoveIds);
        if (paintingImagesToAddIds.size() != paintingImagesToAdd.size()
                || paintingImagesToRemoveIds.size() != paintingImagesToRemove.size()) {
            throw new ResourceListNotFoundException(Sources.IMAGES, Fields.IDS);
        }
        return new Tuple<>(paintingImagesToAdd, paintingImagesToRemove);
    }
}
