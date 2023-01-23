package com.kaliv.myths.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.CriteriaConstants;
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
    public ResponseEntity<PaginatedMythResponseDto> getAllMyths(
            @RequestParam(value = "page", defaultValue = CriteriaConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = CriteriaConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sort", defaultValue = CriteriaConstants.DEFAULT_SORT_ATTRIBUTE, required = false) String sortBy,
            @RequestParam(value = "dir", defaultValue = CriteriaConstants.DEFAULT_SORT_ORDER, required = false) String sortOrder) {
        return ResponseEntity.ok(mythService.getAllMyths(
                pageNumber, pageSize, sortBy, sortOrder));
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
