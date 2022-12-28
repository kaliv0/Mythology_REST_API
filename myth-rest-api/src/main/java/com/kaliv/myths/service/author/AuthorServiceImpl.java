package com.kaliv.myths.service.author;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.dto.authorDtos.AuthorResponseDto;
import com.kaliv.myths.dto.authorDtos.AuthorDto;
import com.kaliv.myths.dto.authorDtos.CreateAuthorDto;
import com.kaliv.myths.dto.authorDtos.UpdateAuthorDto;
import com.kaliv.myths.entity.Nationality;
import com.kaliv.myths.entity.TimePeriod;
import com.kaliv.myths.entity.artefacts.Author;
import com.kaliv.myths.exception.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.ResourceNotFoundException;
import com.kaliv.myths.mapper.GenericMapper;
import com.kaliv.myths.persistence.AuthorRepository;
import com.kaliv.myths.persistence.NationalityRepository;
import com.kaliv.myths.persistence.TimePeriodRepository;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final TimePeriodRepository timePeriodRepository;
    private final NationalityRepository nationalityRepository;
    private final GenericMapper mapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, TimePeriodRepository timePeriodRepository,
                             NationalityRepository nationalityRepository, GenericMapper mapper) {
        this.authorRepository = authorRepository;
        this.timePeriodRepository = timePeriodRepository;
        this.nationalityRepository = nationalityRepository;
        this.mapper = mapper;
    }

    @Override
    public List<AuthorResponseDto> getAllAuthors() {
        return authorRepository.findAll()
                .stream().map(author -> mapper.entityToDto(author, AuthorResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AuthorResponseDto getAuthorById(long id) {
        Author authorInDb = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        return mapper.entityToDto(authorInDb, AuthorResponseDto.class);
    }

    @Override
    public AuthorDto createAuthor(CreateAuthorDto dto) {
        String name = dto.getName();
        //TODO=> rewrite as existsByName
        if (authorRepository.findByName(name).isPresent()) {
            throw new ResourceAlreadyExistsException("Author", "name", name);
        }

        Long timePeriodId = dto.getTimePeriodId();
        if (timePeriodId != null && !timePeriodRepository.existsById(timePeriodId)) {
            throw new ResourceNotFoundException("Time period", "id", timePeriodId);
        }

        Long nationalityId = dto.getNationalityId();
        if (nationalityId != null && !nationalityRepository.existsById(nationalityId)) {
            throw new ResourceNotFoundException("Nationality", "id", nationalityId);
        }

        Author author = mapper.dtoToEntity(dto, Author.class);
        Author savedAuthor = authorRepository.save(author);

        return mapper.entityToDto(savedAuthor, AuthorDto.class);
    }

    @Override
    public AuthorDto updateAuthor(long id, UpdateAuthorDto dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));

        if (Optional.ofNullable(dto.getName()).isPresent()) {
            String name = dto.getName();
            if (!name.equals(author.getName()) && authorRepository.findByName(name).isPresent()) {
                throw new ResourceAlreadyExistsException("Author", "name", name);
            }
            author.setName(dto.getName());
        }

        if (Optional.ofNullable(dto.getTimePeriodId()).isPresent()) {
            long timePeriodId = dto.getTimePeriodId();
            TimePeriod timePeriodInDb = timePeriodRepository.findById(timePeriodId)
                    .orElseThrow(() -> new ResourceNotFoundException("Time period", "id", timePeriodId));
            author.setTimePeriod(timePeriodInDb);
        }

        if (Optional.ofNullable(dto.getNationalityId()).isPresent()) {
            long nationalityId = dto.getNationalityId();
            Nationality nationalityInDb = nationalityRepository.findById(nationalityId)
                    .orElseThrow(() -> new ResourceNotFoundException("Nationality", "id", nationalityId));
            author.setNationality(nationalityInDb);
        }

        Author savedAuthor = authorRepository.save(author);
        return mapper.entityToDto(savedAuthor, AuthorDto.class);
    }

    @Override
    public void deleteAuthor(long id) {
        Author authorInDb = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        authorRepository.delete(authorInDb);
    }
}
