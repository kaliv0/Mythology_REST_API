package com.kaliv.myths.mapper;

import org.modelmapper.ModelMapper;

import com.kaliv.myths.dto.authorDtos.AuthorDto;
import com.kaliv.myths.dto.authorDtos.AuthorResponseDto;
import com.kaliv.myths.dto.authorDtos.CreateAuthorDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Author;

public class AuthorMapper {
    private final ModelMapper mapper;

    public AuthorMapper(ModelMapper mapper) {
        this.mapper = mapper;
        this.mapper.typeMap(CreateAuthorDto.class, Author.class)
                .addMappings(m -> m.skip(BaseEntity::setId))
                .implicitMappings();
    }

    public AuthorDto authorToDto(Author author) {
        return mapper.map(author, AuthorDto.class);
    }

    public AuthorResponseDto authorToResponseDto(Author author) {
        return mapper.map(author, AuthorResponseDto.class);
    }

    public Author dtoToAuthor(CreateAuthorDto dto) {
        return mapper.map(dto, Author.class);
    }
}
