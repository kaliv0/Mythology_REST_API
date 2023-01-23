package com.kaliv.myths.service.author;

import com.kaliv.myths.dto.authorDtos.*;

public interface AuthorService {
    PaginatedAuthorResponseDto getAllAuthors(String timePeriodName,
                                             String nationalityName,
                                             int pageNumber,
                                             int pageSize,
                                             String sortBy,
                                             String sortOrder);

    AuthorResponseDto getAuthorById(long id);

    AuthorDto createAuthor(CreateAuthorDto dto);

    AuthorDto updateAuthor(long id, UpdateAuthorDto dto);

    void deleteAuthor(long id);
}
