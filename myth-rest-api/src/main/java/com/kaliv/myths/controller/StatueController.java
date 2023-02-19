package com.kaliv.myths.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.CriteriaConstants;
import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.statueDtos.*;
import com.kaliv.myths.service.statue.StatueService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Statues")
@RestController
@RequestMapping("/api/v1/statues")
public class StatueController {

    private final StatueService statueService;

    @Autowired
    public StatueController(StatueService statueService) {
        this.statueService = statueService;
    }

    @GetMapping
    public ResponseEntity<PaginatedStatueResponseDto> getAllStatues(
            @RequestParam(name = "author", required = false) String authorName,
            @RequestParam(name = "myth", required = false) String mythName,
            @RequestParam(name = "museum", required = false) String museumName,
            @RequestParam(name = "character", required = false) String characterName,
            @RequestParam(value = "page", defaultValue = CriteriaConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = CriteriaConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sort", defaultValue = CriteriaConstants.DEFAULT_SORT_ATTRIBUTE, required = false) String sortBy,
            @RequestParam(value = "dir", defaultValue = CriteriaConstants.DEFAULT_SORT_ORDER, required = false) String sortOrder) {
        return ResponseEntity.ok(statueService.getAllStatues(
                authorName, mythName, museumName, characterName, pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatueResponseDto> getStatueById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(statueService.getStatueById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('WRITE')")
    public ResponseEntity<StatueDto> createStatue(@Valid @RequestBody CreateStatueDto dto) {
        return new ResponseEntity<>(statueService.createStatue(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('UPDATE')")
    public StatueDto updateStatue(@PathVariable("id") long id, @Valid @RequestBody UpdateStatueDto dto) {
        return statueService.updateStatue(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('DELETE')")
    public ResponseEntity<String> deleteStatue(@PathVariable(name = "id") long id) {
        statueService.deleteStatue(id);
        return new ResponseEntity<>(ResponseMessages.PAINTING_DELETED, HttpStatus.OK);
    }
}