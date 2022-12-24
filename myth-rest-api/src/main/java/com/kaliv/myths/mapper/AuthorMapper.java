package com.kaliv.myths.mapper;

import org.modelmapper.ModelMapper;

import com.kaliv.myths.dto.authorDtos.AuthorDto;
import com.kaliv.myths.dto.authorDtos.CreateUpdateAuthorDto;
import com.kaliv.myths.model.artefacts.Author;


public class AuthorMapper {

    final private static ModelMapper mapper = new ModelMapper();

    public static AuthorDto authorToDto(Author author) {
        return mapper.map(author, AuthorDto.class);
    }

    public static Author dtoToAuthor(CreateUpdateAuthorDto author) {
        return mapper.map(author, Author.class);
    }
}
