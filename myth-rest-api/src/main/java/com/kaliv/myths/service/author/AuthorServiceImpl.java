package com.kaliv.myths.service.author;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.dto.authorDtos.AuthorDto;
import com.kaliv.myths.dto.authorDtos.CreateUpdateAuthorDto;
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

        long nationalityId = dto.getNationalityId();
        Nationality nationalityInDb = nationalityRepository.findById(nationalityId)
                .orElseThrow(() -> new ResourceNotFoundException("Nationality", "id", nationalityId));

        Author author = mapper.dtoToEntity(dto, Author.class);
        Author savedAuthor = authorRepository.save(author);

        timePeriodInDb.getAuthors().add(savedAuthor);
        timePeriodRepository.save(timePeriodInDb);
        nationalityInDb.getAuthors().add(savedAuthor);
        nationalityRepository.save(nationalityInDb);
        return mapper.entityToDto(savedAuthor, AuthorDto.class);
    }

    @Override
    public AuthorDto updateAuthor(long id, CreateUpdateAuthorDto dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));

        String name = dto.getName();
        //authors must have unique names
        if (!name.equals(author.getName()) && authorRepository.findByName(name).isPresent()) {
            throw new ResourceAlreadyExistsException("Author", "name", name);
        }

        long timePeriodId = dto.getTimePeriodId();
        TimePeriod timePeriodInDb = timePeriodRepository.findById(timePeriodId)
                .orElseThrow(() -> new ResourceNotFoundException("TimePeriod", "id", timePeriodId));

        long nationalityId = dto.getNationalityId();
        Nationality nationalityInDb = nationalityRepository.findById(nationalityId)
                .orElseThrow(() -> new ResourceNotFoundException("Nationality", "id", nationalityId));

        //patch update => only new value is given ??
        //TODO: merge with previous check
        if (!dto.getName().equals(author.getName())) {
            author.setName(dto.getName());
        }
        if (dto.getTimePeriodId() != author.getTimePeriod().getId()) {
            author.getTimePeriod().setId(dto.getTimePeriodId());
        }
//        if (dto.getNationalityId() != author.getNationality().getId()) {
//            author.getNationality().setId(dto.getNationalityId());
//        }

        Optional.of(dto.getNationalityId()).ifPresent(author.getNationality()::setId);

        Author savedAuthor = authorRepository.save(author);

        timePeriodInDb.getAuthors().add(savedAuthor);
        timePeriodRepository.save(timePeriodInDb);
        nationalityInDb.getAuthors().add(savedAuthor);
        nationalityRepository.save(nationalityInDb);
        return mapper.entityToDto(savedAuthor, AuthorDto.class);
    }

    @Override
    public void deleteAuthor(long id) {
        Author authorInDb = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        authorRepository.delete(authorInDb);
    }
}
