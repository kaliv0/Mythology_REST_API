package com.kaliv.myths.controller;

import javax.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.authorDtos.AuthorResponseDto;
import com.kaliv.myths.dto.authorDtos.AuthorDto;
import com.kaliv.myths.dto.authorDtos.CreateAuthorDto;
import com.kaliv.myths.dto.authorDtos.UpdateAuthorDto;
import com.kaliv.myths.service.author.AuthorService;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> getAuthorById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody CreateAuthorDto dto) {
        return new ResponseEntity<>(authorService.createAuthor(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public AuthorDto updateAuthor(@PathVariable("id") long id, @Valid @RequestBody UpdateAuthorDto dto) {
        return authorService.updateAuthor(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable(name = "id") long id) {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(ResponseMessages.AUTHOR_DELETED, HttpStatus.OK);
    }
}