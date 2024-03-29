package com.kaliv.myths.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaliv.myths.common.Quadruple;
import com.kaliv.myths.common.Tuple;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.artworkPosessorDtos.CreateArtworkPossessorDto;
import com.kaliv.myths.dto.artworkPosessorDtos.UpdateArtworkPossessorDto;
import com.kaliv.myths.dto.artworkPosessorDtos.UpdateVisualArtworkPossessorDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Music;
import com.kaliv.myths.entity.artefacts.Painting;
import com.kaliv.myths.entity.artefacts.Poem;
import com.kaliv.myths.entity.artefacts.Statue;
import com.kaliv.myths.entity.artefacts.contracts.possessors.ArtworkPossessor;
import com.kaliv.myths.entity.artefacts.contracts.possessors.VisualArtworkPossessor;
import com.kaliv.myths.exception.alreadyExists.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.invalidInput.DuplicateEntriesException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.persistence.MusicRepository;
import com.kaliv.myths.persistence.PaintingRepository;
import com.kaliv.myths.persistence.PoemRepository;
import com.kaliv.myths.persistence.StatueRepository;

@Component
public class ArtworkHandler {
    private final StatueRepository statueRepository;
    private final PaintingRepository paintingRepository;
    private final MusicRepository musicRepository;
    private final PoemRepository poemRepository;

    @Autowired
    public ArtworkHandler(StatueRepository statueRepository,
                          PaintingRepository paintingRepository,
                          MusicRepository musicRepository,
                          PoemRepository poemRepository) {
        this.statueRepository = statueRepository;
        this.paintingRepository = paintingRepository;
        this.musicRepository = musicRepository;
        this.poemRepository = poemRepository;
    }

    public Quadruple<List<Statue>, List<Painting>, List<Music>, List<Poem>> getValidArtworks(CreateArtworkPossessorDto dto) {
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

    public Quadruple<Tuple<List<Statue>, List<Statue>>,
            Tuple<List<Painting>, List<Painting>>,
            Tuple<List<Music>, List<Music>>,
            Tuple<List<Poem>, List<Poem>>> handleArtworksToUpdate(UpdateArtworkPossessorDto dto,
                                                                  ArtworkPossessor artworkPossessorInDb) {
        Tuple<List<Statue>, List<Statue>> statuesToUpdate = this.getValidStatues(dto, artworkPossessorInDb);
        List<Statue> statuesToAdd = statuesToUpdate.getFirst();
        List<Statue> statuesToRemove = statuesToUpdate.getSecond();
        artworkPossessorInDb.getStatues().addAll(new HashSet<>(statuesToAdd));
        artworkPossessorInDb.getStatues().removeAll(new HashSet<>(statuesToRemove));

        Tuple<List<Painting>, List<Painting>> paintingsToUpdate = this.getValidPaintings(dto, artworkPossessorInDb);
        List<Painting> paintingsToAdd = paintingsToUpdate.getFirst();
        List<Painting> paintingsToRemove = paintingsToUpdate.getSecond();
        artworkPossessorInDb.getPaintings().addAll(new HashSet<>(paintingsToAdd));
        artworkPossessorInDb.getPaintings().removeAll(new HashSet<>(paintingsToRemove));

        Tuple<List<Music>, List<Music>> musicToUpdate = this.getValidMusic(dto, artworkPossessorInDb);
        List<Music> musicToAdd = musicToUpdate.getFirst();
        List<Music> musicToRemove = musicToUpdate.getSecond();
        artworkPossessorInDb.getMusic().addAll(new HashSet<>(musicToAdd));
        artworkPossessorInDb.getMusic().removeAll(new HashSet<>(musicToRemove));

        Tuple<List<Poem>, List<Poem>> poemsToUpdate = this.getValidPoems(dto, artworkPossessorInDb);
        List<Poem> poemsToAdd = poemsToUpdate.getFirst();
        List<Poem> poemsToRemove = poemsToUpdate.getSecond();
        artworkPossessorInDb.getPoems().addAll(new HashSet<>(poemsToAdd));
        artworkPossessorInDb.getPoems().removeAll(new HashSet<>(poemsToRemove));

        return new Quadruple<>(statuesToUpdate, paintingsToUpdate, musicToUpdate, poemsToUpdate);
    }

    public Tuple<List<Statue>, List<Statue>> getValidStatues(UpdateVisualArtworkPossessorDto dto,
                                                             VisualArtworkPossessor artworkPossessorInDb) {
        List<Long> statuesToAddIds = new ArrayList<>(dto.getStatuesToAdd());
        List<Long> statuesToRemoveIds = new ArrayList<>(dto.getStatuesToRemove());
        //check if user tries to add and remove same statue
        if (!Collections.disjoint(statuesToAddIds, statuesToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_STATUES, Sources.REMOVE_STATUES);
        }
        //check if user tries to add statue that is already in the list
        if (artworkPossessorInDb.getStatues().stream()
                .map(BaseEntity::getId)
                .anyMatch(statuesToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.STATUE);
        }
        //check if user tries to remove statue that is not in the list
        if (!artworkPossessorInDb.getStatues().stream()
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

    public Tuple<List<Painting>, List<Painting>> getValidPaintings(UpdateVisualArtworkPossessorDto dto,
                                                                   VisualArtworkPossessor artworkPossessorInDb) {
        List<Long> paintingsToAddIds = new ArrayList<>(dto.getPaintingsToAdd());
        List<Long> paintingsToRemoveIds = new ArrayList<>(dto.getPaintingsToRemove());
        //check if user tries to add and remove same painting
        if (!Collections.disjoint(paintingsToAddIds, paintingsToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_PAINTINGS, Sources.REMOVE_PAINTINGS);
        }
        //check if user tries to add painting that is already in the list
        if (artworkPossessorInDb.getPaintings().stream()
                .map(BaseEntity::getId)
                .anyMatch(paintingsToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.PAINTING);
        }
        //check if user tries to remove painting that is not in the list
        if (!artworkPossessorInDb.getPaintings().stream()
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

    public Tuple<List<Music>, List<Music>> getValidMusic(UpdateArtworkPossessorDto dto, ArtworkPossessor artworkPossessorInDb) {
        List<Long> musicToAddIds = new ArrayList<>(dto.getMusicToAdd());
        List<Long> musicToRemoveIds = new ArrayList<>(dto.getMusicToRemove());
        //check if user tries to add and remove same painting
        if (!Collections.disjoint(musicToAddIds, musicToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_MUSIC, Sources.REMOVE_MUSIC);
        }
        //check if user tries to add painting that is already in the list
        if (artworkPossessorInDb.getMusic().stream()
                .map(BaseEntity::getId)
                .anyMatch(musicToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.MUSIC);
        }
        //check if user tries to remove painting that is not in the list
        if (!artworkPossessorInDb.getMusic().stream()
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

    public Tuple<List<Poem>, List<Poem>> getValidPoems(UpdateArtworkPossessorDto dto, ArtworkPossessor artworkPossessorInDb) {
        List<Long> poemsToAddIds = new ArrayList<>(dto.getPoemsToAdd());
        List<Long> poemsToRemoveIds = new ArrayList<>(dto.getPoemsToRemove());
        //check if user tries to add and remove same painting
        if (!Collections.disjoint(poemsToAddIds, poemsToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_POEMS, Sources.REMOVE_POEMS);
        }
        //check if user tries to add painting that is already in the list
        if (artworkPossessorInDb.getPoems().stream()
                .map(BaseEntity::getId)
                .anyMatch(poemsToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.POEM);
        }
        //check if user tries to remove painting that is not in the list
        if (!artworkPossessorInDb.getPoems().stream()
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
