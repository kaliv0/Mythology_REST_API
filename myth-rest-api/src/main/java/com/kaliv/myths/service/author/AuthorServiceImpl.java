package com.kaliv.myths.service.author;

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
import com.kaliv.myths.dto.authorDtos.*;
import com.kaliv.myths.dto.authorDtos.UpdateAuthorDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Author;
import com.kaliv.myths.entity.Nationality;
import com.kaliv.myths.entity.TimePeriod;
import com.kaliv.myths.entity.artefacts.*;
import com.kaliv.myths.exception.alreadyExists.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.invalidInput.DuplicateEntriesException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.AuthorMapper;
import com.kaliv.myths.persistence.*;
import com.querydsl.core.BooleanBuilder;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final TimePeriodRepository timePeriodRepository;
    private final NationalityRepository nationalityRepository;
    private final StatueRepository statueRepository;
    private final PaintingRepository paintingRepository;
    private final MusicRepository musicRepository;
    private final PoemRepository poemRepository;
    private final AuthorMapper mapper;

    public AuthorServiceImpl(AuthorRepository authorRepository,
                             TimePeriodRepository timePeriodRepository,
                             NationalityRepository nationalityRepository,
                             StatueRepository statueRepository,
                             PaintingRepository paintingRepository,
                             MusicRepository musicRepository,
                             PoemRepository poemRepository,
                             AuthorMapper mapper) {
        this.authorRepository = authorRepository;
        this.timePeriodRepository = timePeriodRepository;
        this.nationalityRepository = nationalityRepository;
        this.statueRepository = statueRepository;
        this.paintingRepository = paintingRepository;
        this.musicRepository = musicRepository;
        this.poemRepository = poemRepository;
        this.mapper = mapper;
    }

    @Override
    public PaginatedAuthorResponseDto getAllAuthors(String timePeriodName,
                                                    String nationalityName,
                                                    int pageNumber,
                                                    int pageSize,
                                                    String sortBy,
                                                    String sortOrder) {
        QAuthor qAuthor = QAuthor.author;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (timePeriodName != null) {
            booleanBuilder.and(qAuthor.timePeriod.name.equalsIgnoreCase(timePeriodName));
        }
        if (nationalityName != null) {
            booleanBuilder.and(qAuthor.nationality.name.equalsIgnoreCase(nationalityName));
        }

        Sort sortCriteria = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortCriteria);
        Page<Author> authors = authorRepository.findAll(booleanBuilder, pageable);

        List<AuthorResponseDto> content = authors
                .getContent().stream()
                .map(mapper::authorToResponseDto)
                .collect(Collectors.toList());

        PaginatedAuthorResponseDto authorResponseDto = new PaginatedAuthorResponseDto();
        authorResponseDto.setContent(content);
        authorResponseDto.setPageNumber(authors.getNumber());
        authorResponseDto.setPageSize(authors.getSize());
        authorResponseDto.setTotalElements(authors.getTotalElements());
        authorResponseDto.setTotalPages(authors.getTotalPages());
        authorResponseDto.setLast(authors.isLast());

        return authorResponseDto;
    }

    @Override
    public AuthorResponseDto getAuthorById(long id) {
        Author authorInDb = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.AUTHOR, Fields.ID, id));
        return mapper.authorToResponseDto(authorInDb);
    }

    @Override
    public AuthorDto createAuthor(CreateAuthorDto dto) {
        String name = dto.getName();
        if (authorRepository.existsByName(name)) {
            throw new ResourceWithGivenValuesExistsException(Sources.AUTHOR, Fields.NAME, name);
        }

        Long timePeriodId = dto.getTimePeriodId();
        if (timePeriodId != null && !timePeriodRepository.existsById(timePeriodId)) {
            throw new ResourceWithGivenValuesNotFoundException(Sources.TIME_PERIOD, Fields.ID, timePeriodId);
        }

        Long nationalityId = dto.getNationalityId();
        if (nationalityId != null && !nationalityRepository.existsById(nationalityId)) {
            throw new ResourceWithGivenValuesNotFoundException(Sources.NATIONALITY, Fields.ID, nationalityId);
        }

        Quadruple<List<Statue>, List<Painting>, List<Music>, List<Poem>> artworks = this.getValidArtworks(dto);
        List<Statue> statues = artworks.getFirst();
        List<Painting> paintings = artworks.getSecond();
        List<Music> music = artworks.getThird();
        List<Poem> poems = artworks.getFourth();

        Author author = mapper.dtoToAuthor(dto);
        author.setStatues(new HashSet<>(statues));
        author.setPaintings(new HashSet<>(paintings));
        author.setMusic(new HashSet<>(music));
        author.setPoems(new HashSet<>(poems));
        Author savedAuthor = authorRepository.save(author);

        return mapper.authorToDto(savedAuthor);
    }

    @Override
    public AuthorDto updateAuthor(long id, UpdateAuthorDto dto) {
        Author authorInDb = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.AUTHOR, Fields.ID, id));

        if (Optional.ofNullable(dto.getName()).isPresent()) {
            String newName = dto.getName();
            if (!newName.equals(authorInDb.getName()) && authorRepository.existsByName(newName)) {
                throw new ResourceWithGivenValuesExistsException(Sources.AUTHOR, Fields.NAME, newName);
            }
            authorInDb.setName(dto.getName());
        }
        if (Optional.ofNullable(dto.getTimePeriodId()).isPresent()) {
            long timePeriodId = dto.getTimePeriodId();
            TimePeriod timePeriodInDb = timePeriodRepository.findById(timePeriodId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.TIME_PERIOD, Fields.ID, timePeriodId));
            authorInDb.setTimePeriod(timePeriodInDb);
        }
        if (Optional.ofNullable(dto.getNationalityId()).isPresent()) {
            long nationalityId = dto.getNationalityId();
            Nationality nationalityInDb = nationalityRepository.findById(nationalityId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.NATIONALITY, Fields.ID, nationalityId));
            authorInDb.setNationality(nationalityInDb);
        }

        handleCollectionsToUpdate(dto, authorInDb);
        authorRepository.save(authorInDb);
        return mapper.authorToDto(authorInDb);
    }

    @Override
    public void deleteAuthor(long id) {
        Author authorInDb = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.AUTHOR, Fields.ID, id));
        authorRepository.delete(authorInDb);
    }

    private Quadruple<List<Statue>, List<Painting>, List<Music>, List<Poem>> getValidArtworks(CreateAuthorDto dto) {
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

    private void handleCollectionsToUpdate(UpdateAuthorDto dto, Author authorInDb) {
        Tuple<List<Statue>, List<Statue>> statuesToUpdate = this.getValidStatues(dto, authorInDb);
        List<Statue> statuesToAdd = statuesToUpdate.getFirst();
        List<Statue> statuesToRemove = statuesToUpdate.getSecond();
        authorInDb.getStatues().addAll(new HashSet<>(statuesToAdd));
        authorInDb.getStatues().removeAll(new HashSet<>(statuesToRemove));

        Tuple<List<Painting>, List<Painting>> paintingsToUpdate = this.getValidPaintings(dto, authorInDb);
        List<Painting> paintingsToAdd = paintingsToUpdate.getFirst();
        List<Painting> paintingsToRemove = paintingsToUpdate.getSecond();
        authorInDb.getPaintings().addAll(new HashSet<>(paintingsToAdd));
        authorInDb.getPaintings().removeAll(new HashSet<>(paintingsToRemove));

        Tuple<List<Music>, List<Music>> musicToUpdate = this.getValidMusic(dto, authorInDb);
        List<Music> musicToAdd = musicToUpdate.getFirst();
        List<Music> musicToRemove = musicToUpdate.getSecond();
        authorInDb.getMusic().addAll(new HashSet<>(musicToAdd));
        authorInDb.getMusic().removeAll(new HashSet<>(musicToRemove));

        Tuple<List<Poem>, List<Poem>> poemsToUpdate = this.getValidPoems(dto, authorInDb);
        List<Poem> poemsToAdd = poemsToUpdate.getFirst();
        List<Poem> poemsToRemove = poemsToUpdate.getSecond();
        authorInDb.getPoems().addAll(new HashSet<>(poemsToAdd));
        authorInDb.getPoems().removeAll(new HashSet<>(poemsToRemove));
    }

    private Tuple<List<Statue>, List<Statue>> getValidStatues(UpdateAuthorDto dto, Author authorInDb) {
        List<Long> statuesToAddIds = new ArrayList<>(dto.getStatuesToAdd());
        List<Long> statuesToRemoveIds = new ArrayList<>(dto.getStatuesToRemove());
        //check if user tries to add and remove same statue
        if (!Collections.disjoint(statuesToAddIds, statuesToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_STATUES, Sources.REMOVE_STATUES);
        }
        //check if user tries to add statue that is already in the list
        if (authorInDb.getStatues().stream()
                .map(BaseEntity::getId)
                .anyMatch(statuesToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.STATUE);
        }
        //check if user tries to remove statue that is not in the list
        if (!authorInDb.getStatues().stream()
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

    private Tuple<List<Painting>, List<Painting>> getValidPaintings(UpdateAuthorDto dto, Author authorInDb) {
        List<Long> paintingsToAddIds = new ArrayList<>(dto.getPaintingsToAdd());
        List<Long> paintingsToRemoveIds = new ArrayList<>(dto.getPaintingsToRemove());
        //check if user tries to add and remove same painting
        if (!Collections.disjoint(paintingsToAddIds, paintingsToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_PAINTINGS, Sources.REMOVE_PAINTINGS);
        }
        //check if user tries to add painting that is already in the list
        if (authorInDb.getPaintings().stream()
                .map(BaseEntity::getId)
                .anyMatch(paintingsToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.PAINTING);
        }
        //check if user tries to remove painting that is not in the list
        if (!authorInDb.getPaintings().stream()
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

    private Tuple<List<Music>, List<Music>> getValidMusic(UpdateAuthorDto dto, Author authorInDb) {
        List<Long> musicToAddIds = new ArrayList<>(dto.getMusicToAdd());
        List<Long> musicToRemoveIds = new ArrayList<>(dto.getMusicToRemove());
        //check if user tries to add and remove same painting
        if (!Collections.disjoint(musicToAddIds, musicToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_MUSIC, Sources.REMOVE_MUSIC);
        }
        //check if user tries to add painting that is already in the list
        if (authorInDb.getMusic().stream()
                .map(BaseEntity::getId)
                .anyMatch(musicToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.MUSIC);
        }
        //check if user tries to remove painting that is not in the list
        if (!authorInDb.getMusic().stream()
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

    private Tuple<List<Poem>, List<Poem>> getValidPoems(UpdateAuthorDto dto, Author authorInDb) {
        List<Long> poemsToAddIds = new ArrayList<>(dto.getPoemsToAdd());
        List<Long> poemsToRemoveIds = new ArrayList<>(dto.getPoemsToRemove());
        //check if user tries to add and remove same painting
        if (!Collections.disjoint(poemsToAddIds, poemsToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_POEMS, Sources.REMOVE_POEMS);
        }
        //check if user tries to add painting that is already in the list
        if (authorInDb.getPoems().stream()
                .map(BaseEntity::getId)
                .anyMatch(poemsToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.POEM);
        }
        //check if user tries to remove painting that is not in the list
        if (!authorInDb.getPoems().stream()
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
