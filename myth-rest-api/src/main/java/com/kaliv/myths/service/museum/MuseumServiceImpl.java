package com.kaliv.myths.service.museum;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.common.containers.Tuple;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.museumDtos.CreateMuseumDto;
import com.kaliv.myths.dto.museumDtos.MuseumDto;
import com.kaliv.myths.dto.museumDtos.MuseumResponseDto;
import com.kaliv.myths.dto.museumDtos.UpdateMuseumDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Museum;
import com.kaliv.myths.entity.artefacts.Painting;
import com.kaliv.myths.entity.artefacts.Statue;
import com.kaliv.myths.exception.alreadyExists.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.invalidInput.DuplicateEntriesException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.MuseumMapper;
import com.kaliv.myths.persistence.MuseumRepository;
import com.kaliv.myths.persistence.PaintingRepository;
import com.kaliv.myths.persistence.StatueRepository;

@Service
public class MuseumServiceImpl implements MuseumService {

    private final MuseumRepository museumRepository;
    private final StatueRepository statueRepository;
    private final PaintingRepository paintingRepository;
    private final MuseumMapper mapper;

    public MuseumServiceImpl(MuseumRepository museumRepository,
                             StatueRepository statueRepository,
                             PaintingRepository paintingRepository,
                             MuseumMapper mapper) {
        this.museumRepository = museumRepository;
        this.statueRepository = statueRepository;
        this.paintingRepository = paintingRepository;
        this.mapper = mapper;
    }

    @Override
    public List<MuseumResponseDto> getAllMuseums() {
        return museumRepository.findAll()
                .stream().map(mapper::museumToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public MuseumResponseDto getMuseumById(long id) {
        Museum museumInDb = museumRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MUSEUM, Fields.ID, id));
        return mapper.museumToResponseDto(museumInDb);
    }

    @Override
    public MuseumDto createMuseum(CreateMuseumDto dto) {
        String name = dto.getName();
        if (museumRepository.existsByName(name)) {
            throw new ResourceWithGivenValuesExistsException(Sources.MUSEUM, Fields.NAME, name);
        }

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

        Museum museum = mapper.dtoToMuseum(dto);
        museum.setStatues(new HashSet<>(statues));
        museum.setPaintings(new HashSet<>(paintings));
        Museum savedMuseum = museumRepository.save(museum);

        return mapper.museumToDto(savedMuseum);
    }

    @Override
    public MuseumDto updateMuseum(long id, UpdateMuseumDto dto) {
        Museum museumInDb = museumRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MUSEUM, Fields.ID, id));

        if (Optional.ofNullable(dto.getName()).isPresent()) {
            String newName = dto.getName();
            if (!newName.equals(museumInDb.getName()) && museumRepository.existsByName(newName)) {
                throw new ResourceWithGivenValuesExistsException(Sources.MUSEUM, Fields.NAME, newName);
            }
            museumInDb.setName(dto.getName());
        }

        Tuple<List<Statue>, List<Statue>> statuesToUpdate = this.getValidStatues(dto, museumInDb);
        List<Statue> statuesToAdd = statuesToUpdate.getFirst();
        List<Statue> statuesToRemove = statuesToUpdate.getSecond();
        museumInDb.getStatues().addAll(new HashSet<>(statuesToAdd));
        museumInDb.getStatues().removeAll(new HashSet<>(statuesToRemove));

        Tuple<List<Painting>, List<Painting>> paintingsToUpdate = this.getValidPaintings(dto, museumInDb);
        List<Painting> paintingsToAdd = paintingsToUpdate.getFirst();
        List<Painting> paintingsToRemove = paintingsToUpdate.getSecond();
        museumInDb.getPaintings().addAll(new HashSet<>(paintingsToAdd));
        museumInDb.getPaintings().removeAll(new HashSet<>(paintingsToRemove));

        museumRepository.save(museumInDb);

        statuesToAdd.forEach(m -> m.setMuseum(museumInDb));
        statueRepository.saveAll(statuesToAdd);
        statuesToRemove.forEach(m -> m.setMuseum(null));
        statueRepository.saveAll(statuesToRemove);

        paintingsToAdd.forEach(a -> a.setMuseum(museumInDb));
        paintingRepository.saveAll(paintingsToAdd);
        paintingsToRemove.forEach(a -> a.setMuseum(null));
        paintingRepository.saveAll(paintingsToRemove);

        return mapper.museumToDto(museumInDb);
    }

    @Override
    public void deleteMuseum(long id) {
        Museum museumInDb = museumRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MUSEUM, Fields.ID, id));
        museumRepository.delete(museumInDb);
    }

    private Tuple<List<Statue>, List<Statue>> getValidStatues(UpdateMuseumDto dto, Museum museumInDb) {
        List<Long> statuesToAddIds = new ArrayList<>(dto.getStatuesToAdd());
        List<Long> statuesToRemoveIds = new ArrayList<>(dto.getStatuesToRemove());
        //check if user tries to add and remove same statue
        if (!Collections.disjoint(statuesToAddIds, statuesToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_STATUES, Sources.REMOVE_STATUES);
        }
        //check if user tries to add statue that is already in the list
        if (museumInDb.getStatues().stream()
                .map(BaseEntity::getId)
                .anyMatch(statuesToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.STATUE);
        }
        //check if user tries to remove statue that is not in the list
        if (!museumInDb.getStatues().stream()
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

    private Tuple<List<Painting>, List<Painting>> getValidPaintings(UpdateMuseumDto dto, Museum museumInDb) {
        List<Long> paintingsToAddIds = new ArrayList<>(dto.getPaintingsToAdd());
        List<Long> paintingsToRemoveIds = new ArrayList<>(dto.getPaintingsToRemove());
        //check if user tries to add and remove same painting
        if (!Collections.disjoint(paintingsToAddIds, paintingsToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_PAINTINGS, Sources.REMOVE_PAINTINGS);
        }
        //check if user tries to add painting that is already in the list
        if (museumInDb.getPaintings().stream()
                .map(BaseEntity::getId)
                .anyMatch(paintingsToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.PAINTING);
        }
        //check if user tries to remove painting that is not in the list
        if (!museumInDb.getPaintings().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet())
                .containsAll(paintingsToRemoveIds)) {
            throw new ResourceNotFoundException(Sources.PAINTING);
        }

        List<Painting> paintingsToAdd = paintingRepository.findAllById(paintingsToAddIds);
        List<Painting> paintingsToRemove = paintingRepository.findAllById(paintingsToRemoveIds);
        if (paintingsToAddIds.size() != paintingsToAdd.size()
                || paintingsToRemoveIds.size() != paintingsToRemove.size()) {
            throw new ResourceListNotFoundException(Sources.PAINTINGS, Fields.IDS);
        }
        return new Tuple<>(paintingsToAdd, paintingsToRemove);
    }
}
