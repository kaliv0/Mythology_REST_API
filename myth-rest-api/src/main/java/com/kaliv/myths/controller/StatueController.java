package com.kaliv.myths.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.common.criteria.PaginationCriteria;
import com.kaliv.myths.common.criteria.SortCriteria;
import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.statueDtos.*;
import com.kaliv.myths.service.statue.StatueService;

@RestController
@RequestMapping("/api/v1/statues")
public class StatueController {

    private final StatueService statueService;

    public StatueController(StatueService statueService) {
        this.statueService = statueService;
    }

    @GetMapping
    public ResponseEntity<PaginatedStatueResponseDto> getAllStatues(
            @RequestParam(name = "author", required = false) String authorName,
            @RequestParam(name = "myth", required = false) String mythName,
            @RequestParam(name = "museum", required = false) String museumName,
            @RequestParam(name = "character", required = false) String characterName,
            PaginationCriteria paginationCriteria,
            SortCriteria sortCriteria) {
        return ResponseEntity.ok(statueService.getAllStatues(
                authorName, mythName, museumName, characterName, paginationCriteria, sortCriteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatueResponseDto> getStatueById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(statueService.getStatueById(id));
    }

    @PostMapping
    public ResponseEntity<StatueDto> createStatue(@Valid @RequestBody CreateStatueDto dto) {
        return new ResponseEntity<>(statueService.createStatue(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public StatueDto updateStatue(@PathVariable("id") long id, @Valid @RequestBody UpdateStatueDto dto) {
        return statueService.updateStatue(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStatue(@PathVariable(name = "id") long id) {
        statueService.deleteStatue(id);
        return new ResponseEntity<>(ResponseMessages.PAINTING_DELETED, HttpStatus.OK);
    }
}