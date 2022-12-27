package com.kaliv.myths.service.author;

import java.util.List;

import com.kaliv.myths.dto.authorDtos.CreateUpdateAuthorDto;
import com.kaliv.myths.dto.authorDtos.AuthorDto;

public interface AuthorService {
    List<AuthorDto> getAllAuthors();

    AuthorDto getAuthorById(long id);

    AuthorDto createAuthor(CreateUpdateAuthorDto dto);

    AuthorDto updateAuthor(long id, CreateUpdateAuthorDto dto);

    void deleteAuthor(long id);
}
