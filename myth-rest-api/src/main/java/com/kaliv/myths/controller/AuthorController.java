package com.kaliv.myths.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.CriteriaConstants;
import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.authorDtos.*;
import com.kaliv.myths.service.author.AuthorService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authors")
@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<PaginatedAuthorResponseDto> getAllAuthors(
            @RequestParam(name = "time-period", required = false) String timePeriodName,
            @RequestParam(name = "nationality", required = false) String nationalityName,
            @RequestParam(value = "page", defaultValue = CriteriaConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = CriteriaConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sort", defaultValue = CriteriaConstants.DEFAULT_SORT_ATTRIBUTE, required = false) String sortBy,
            @RequestParam(value = "dir", defaultValue = CriteriaConstants.DEFAULT_SORT_ORDER, required = false) String sortOrder) {
        return ResponseEntity.ok(authorService.getAllAuthors(
                timePeriodName, nationalityName, pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> getAuthorById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole({'STAFF','ADMIN'})")
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody CreateAuthorDto dto) {
        return new ResponseEntity<>(authorService.createAuthor(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole({'STAFF','ADMIN'})")
    public AuthorDto updateAuthor(@PathVariable("id") long id, @Valid @RequestBody UpdateAuthorDto dto) {
        return authorService.updateAuthor(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAuthor(@PathVariable(name = "id") long id) {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(ResponseMessages.AUTHOR_DELETED, HttpStatus.OK);
    }
}