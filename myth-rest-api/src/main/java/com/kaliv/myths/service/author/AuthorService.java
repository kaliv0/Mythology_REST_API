package com.kaliv.myths.service.author;

import java.util.List;

import com.kaliv.myths.dto.authorDtos.AuthorDto;
import com.kaliv.myths.dto.authorDtos.CreateAuthorDto;
import com.kaliv.myths.dto.authorDtos.AuthorResponseDto;
import com.kaliv.myths.dto.authorDtos.UpdateAuthorDto;

public interface AuthorService {
    List<AuthorResponseDto> getAllAuthors();

    AuthorResponseDto getAuthorById(long id);

    AuthorDto createAuthor(CreateAuthorDto dto);

    AuthorDto updateAuthor(long id, UpdateAuthorDto dto);

    void deleteAuthor(long id);
}
