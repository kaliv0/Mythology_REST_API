package com.kaliv.myths.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.CriteriaConstants;
import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.poemDtos.*;
import com.kaliv.myths.service.poem.PoemService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Poems")
@RestController
@RequestMapping("/api/v1/poems")
public class PoemController {

    private final PoemService poemService;

    public PoemController(PoemService poemService) {
        this.poemService = poemService;
    }

    @GetMapping
    public ResponseEntity<PaginatedPoemResponseDto> getAllPoems(
            @RequestParam(name = "author", required = false) String authorName,
            @RequestParam(name = "myth", required = false) String mythName,
            @RequestParam(name = "character", required = false) String characterName,
            @RequestParam(value = "page", defaultValue = CriteriaConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = CriteriaConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sort", defaultValue = CriteriaConstants.DEFAULT_SORT_ATTRIBUTE, required = false) String sortBy,
            @RequestParam(value = "dir", defaultValue = CriteriaConstants.DEFAULT_SORT_ORDER, required = false) String sortOrder) {
        return ResponseEntity.ok(poemService.getAllPoems(
                authorName, mythName, characterName, pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PoemResponseDto> getPoemById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(poemService.getPoemById(id));
    }

    @PostMapping
    public ResponseEntity<PoemDto> createPoem(@Valid @RequestBody CreatePoemDto dto) {
        return new ResponseEntity<>(poemService.createPoem(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public PoemDto updatePoem(@PathVariable("id") long id, @Valid @RequestBody UpdatePoemDto dto) {
        return poemService.updatePoem(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePoem(@PathVariable(name = "id") long id) {
        poemService.deletePoem(id);
        return new ResponseEntity<>(ResponseMessages.POEM_DELETED, HttpStatus.OK);
    }
}