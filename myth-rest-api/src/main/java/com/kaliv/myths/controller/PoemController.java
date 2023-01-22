package com.kaliv.myths.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.common.criteria.PaginationCriteria;
import com.kaliv.myths.common.criteria.SortCriteria;
import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.poemDtos.*;
import com.kaliv.myths.service.poem.PoemService;

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
            PaginationCriteria paginationCriteria,
            SortCriteria sortCriteria) {
        return ResponseEntity.ok(poemService.getAllPoems(
                authorName, mythName, characterName, paginationCriteria, sortCriteria));
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