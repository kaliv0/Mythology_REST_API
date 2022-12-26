package com.kaliv.myths.service.author;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.dto.authorDtos.AuthorDto;
import com.kaliv.myths.dto.authorDtos.CreateUpdateAuthorDto;
import com.kaliv.myths.exception.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.ResourceNotFoundException;
import com.kaliv.myths.mapper.GenericMapper;
import com.kaliv.myths.entity.TimePeriod;
import com.kaliv.myths.entity.artefacts.Author;
import com.kaliv.myths.persistence.AuthorRepository;
import com.kaliv.myths.persistence.TimePeriodRepository;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final TimePeriodRepository timePeriodRepository;
    private final GenericMapper mapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, TimePeriodRepository timePeriodRepository, GenericMapper mapper) {
        this.authorRepository = authorRepository;
        this.timePeriodRepository = timePeriodRepository;
        this.mapper = mapper;
    }

    @Override
    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll()
                .stream().map(author -> mapper.entityToDto(author, AuthorDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDto getAuthorById(long id) {
        Author authorInDb = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        return mapper.entityToDto(authorInDb, AuthorDto.class);
    }

    @Override
    public AuthorDto createAuthor(CreateUpdateAuthorDto dto) {
        String name = dto.getName();
        if (authorRepository.findByName(name).isPresent()) {
            throw new ResourceAlreadyExistsException("Author", "name", name);
        }

        long timePeriodId = dto.getTimePeriodId();
        TimePeriod timePeriodInDb = timePeriodRepository.findById(timePeriodId)
                .orElseThrow(() -> new ResourceNotFoundException("TimePeriod", "id", timePeriodId));


        Author author = mapper.dtoToEntity(dto, Author.class);
        Author savedAuthor = authorRepository.save(author);

        timePeriodInDb.getAuthors().add(savedAuthor);
        timePeriodRepository.saveAndFlush(timePeriodInDb);
        return mapper.entityToDto(savedAuthor, AuthorDto.class);
    }

    @Override
    public AuthorDto updateAuthor(long id, CreateUpdateAuthorDto dto) {
        return null;
    }

    @Override
    public void deleteAuthor(long id) {
        Author authorInDb = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        authorRepository.delete(authorInDb);
    }
}
