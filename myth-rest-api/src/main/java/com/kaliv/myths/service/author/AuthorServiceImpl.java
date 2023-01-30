package com.kaliv.myths.service.author;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kaliv.myths.common.ArtworkHandler;
import com.kaliv.myths.common.container.Quadruple;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.authorDtos.*;
import com.kaliv.myths.entity.Nationality;
import com.kaliv.myths.entity.TimePeriod;
import com.kaliv.myths.entity.artefacts.*;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.AuthorMapper;
import com.kaliv.myths.persistence.AuthorRepository;
import com.kaliv.myths.persistence.NationalityRepository;
import com.kaliv.myths.persistence.TimePeriodRepository;
import com.querydsl.core.BooleanBuilder;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final TimePeriodRepository timePeriodRepository;
    private final NationalityRepository nationalityRepository;
    private final ArtworkHandler artworkHandler;
    private final AuthorMapper mapper;

    public AuthorServiceImpl(AuthorRepository authorRepository,
                             TimePeriodRepository timePeriodRepository,
                             NationalityRepository nationalityRepository,
                             ArtworkHandler artworkHandler,
                             AuthorMapper mapper) {
        this.authorRepository = authorRepository;
        this.timePeriodRepository = timePeriodRepository;
        this.nationalityRepository = nationalityRepository;
        this.artworkHandler = artworkHandler;
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

        Quadruple<List<Statue>, List<Painting>, List<Music>, List<Poem>> artworks = artworkHandler.getValidArtworks(dto);
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

        artworkHandler.handleArtworksToUpdate(dto, authorInDb);
        authorRepository.save(authorInDb);
        return mapper.authorToDto(authorInDb);
    }

    @Override
    public void deleteAuthor(long id) {
        Author authorInDb = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.AUTHOR, Fields.ID, id));
        authorRepository.delete(authorInDb);
    }
}
