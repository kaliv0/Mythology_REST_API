package com.kaliv.myths.service.museum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.common.ArtworkHandler;
import com.kaliv.myths.common.container.Tuple;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.museumDtos.CreateMuseumDto;
import com.kaliv.myths.dto.museumDtos.MuseumDto;
import com.kaliv.myths.dto.museumDtos.MuseumResponseDto;
import com.kaliv.myths.dto.museumDtos.UpdateMuseumDto;
import com.kaliv.myths.entity.artefacts.Museum;
import com.kaliv.myths.entity.artefacts.Painting;
import com.kaliv.myths.entity.artefacts.Statue;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
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
    private final ArtworkHandler artworkHandler;
    private final MuseumMapper mapper;

    public MuseumServiceImpl(MuseumRepository museumRepository,
                             StatueRepository statueRepository,
                             PaintingRepository paintingRepository,
                             ArtworkHandler artworkHandler,
                             MuseumMapper mapper) {
        this.museumRepository = museumRepository;
        this.statueRepository = statueRepository;
        this.paintingRepository = paintingRepository;
        this.artworkHandler = artworkHandler;
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

        this.handleCollectionsToUpdate(dto, museumInDb);
        museumRepository.save(museumInDb);
        return mapper.museumToDto(museumInDb);
    }

    @Override
    public void deleteMuseum(long id) {
        Museum museumInDb = museumRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MUSEUM, Fields.ID, id));
        museumRepository.delete(museumInDb);
    }

    private void handleCollectionsToUpdate(UpdateMuseumDto dto, Museum museumInDb) {
        Tuple<List<Statue>, List<Statue>> statuesToUpdate = artworkHandler.getValidStatues(dto, museumInDb);
        List<Statue> statuesToAdd = statuesToUpdate.getFirst();
        List<Statue> statuesToRemove = statuesToUpdate.getSecond();
        museumInDb.getStatues().addAll(new HashSet<>(statuesToAdd));
        museumInDb.getStatues().removeAll(new HashSet<>(statuesToRemove));

        Tuple<List<Painting>, List<Painting>> paintingsToUpdate = artworkHandler.getValidPaintings(dto, museumInDb);
        List<Painting> paintingsToAdd = paintingsToUpdate.getFirst();
        List<Painting> paintingsToRemove = paintingsToUpdate.getSecond();
        museumInDb.getPaintings().addAll(new HashSet<>(paintingsToAdd));
        museumInDb.getPaintings().removeAll(new HashSet<>(paintingsToRemove));

        statuesToAdd.forEach(statue -> statue.setMuseum(museumInDb));
        statueRepository.saveAll(statuesToAdd);
        statuesToRemove.forEach(statue -> statue.setMuseum(null));
        statueRepository.saveAll(statuesToRemove);

        paintingsToAdd.forEach(painting -> painting.setMuseum(museumInDb));
        paintingRepository.saveAll(paintingsToAdd);
        paintingsToRemove.forEach(painting -> painting.setMuseum(null));
        paintingRepository.saveAll(paintingsToRemove);
    }
}
