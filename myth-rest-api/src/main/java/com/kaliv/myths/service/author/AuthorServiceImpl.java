package com.kaliv.myths.service.author;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.dto.authorDtos.AuthorDto;
import com.kaliv.myths.dto.authorDtos.CreateUpdateAuthorDto;
import com.kaliv.myths.exception.ResourceNotFoundException;
import com.kaliv.myths.mapper.GenericMapper;
import com.kaliv.myths.model.artefacts.Author;
import com.kaliv.myths.persistence.AuthorRepository;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final GenericMapper mapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, GenericMapper mapper) {
        this.authorRepository = authorRepository;
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
        return null;
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
