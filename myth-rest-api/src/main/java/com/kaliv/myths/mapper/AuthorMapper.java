package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.authorDtos.AuthorDto;
import com.kaliv.myths.dto.authorDtos.AuthorResponseDto;
import com.kaliv.myths.dto.authorDtos.CreateAuthorDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Author;

public class AuthorMapper {
    private final ModelMapper mapper;

    @Autowired
    public AuthorMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public AuthorDto authorToDto(Author author) {
        AuthorDto authorDto = mapper.map(author, AuthorDto.class);
        authorDto.setStatueIds(
                author.getStatues().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        authorDto.setPaintingIds(
                author.getPaintings().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        authorDto.setMusicIds(
                author.getMusic().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        authorDto.setPoemIds(
                author.getPoems().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return authorDto;
    }

    public AuthorResponseDto authorToResponseDto(Author author) {
        AuthorResponseDto authorResponseDto = mapper.map(author, AuthorResponseDto.class);
        authorResponseDto.setStatues(
                author.getStatues().stream()
                        .map(statue -> mapper.map(statue, BaseDto.class))
                        .collect(Collectors.toSet()));
        authorResponseDto.setPaintings(
                author.getPaintings().stream()
                        .map(painting -> mapper.map(painting, BaseDto.class))
                        .collect(Collectors.toSet()));
        authorResponseDto.setMusic(
                author.getMusic().stream()
                        .map(music -> mapper.map(music, BaseDto.class))
                        .collect(Collectors.toSet()));
        authorResponseDto.setPoems(
                author.getPoems().stream()
                        .map(poem -> mapper.map(poem, BaseDto.class))
                        .collect(Collectors.toSet()));
        return authorResponseDto;
    }

    public Author dtoToAuthor(CreateAuthorDto dto) {
        return mapper.map(dto, Author.class);
    }
}
