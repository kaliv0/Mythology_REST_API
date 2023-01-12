package com.kaliv.myths.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.common.criteria.PaginationCriteria;
import com.kaliv.myths.common.criteria.SortCriteria;
import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.mythDtos.*;
import com.kaliv.myths.service.myth.MythService;

@RestController
@RequestMapping("/api/v1/myths")
public class MythController {

    private final MythService mythService;

    public MythController(MythService mythService) {
        this.mythService = mythService;
    }

    @GetMapping
    public ResponseEntity<PaginatedMythResponseDto> getAllMyths(PaginationCriteria paginationCriteria, SortCriteria sortCriteria) {
        return ResponseEntity.ok(mythService.getAllMyths(paginationCriteria, sortCriteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MythResponseDto> getMythById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(mythService.getMythById(id));
    }

    @PostMapping
    public ResponseEntity<MythDto> createMyth(@Valid @RequestBody CreateMythDto dto) {
        return new ResponseEntity<>(mythService.createMyth(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public MythDto updateMyth(@PathVariable("id") Long id, @Valid @RequestBody UpdateMythDto dto) {
        return mythService.updateMyth(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMyth(@PathVariable(name = "id") long id) {
        mythService.deleteMyth(id);
        return new ResponseEntity<>(ResponseMessages.MYTH_DELETED, HttpStatus.OK);
    }
}
