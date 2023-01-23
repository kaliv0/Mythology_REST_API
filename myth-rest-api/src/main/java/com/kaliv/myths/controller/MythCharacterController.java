package com.kaliv.myths.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.CriteriaConstants;
import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.mythCharacterDtos.*;
import com.kaliv.myths.service.mythCharacter.MythCharacterService;

@RestController
@RequestMapping("/api/v1/myth-characters")
public class MythCharacterController {

    private final MythCharacterService mythCharacterService;

    public MythCharacterController(MythCharacterService mythCharacterService) {
        this.mythCharacterService = mythCharacterService;
    }

    @GetMapping
    public ResponseEntity<PaginatedMythCharacterResponseDto> getAllMythCharacters(
            @RequestParam(name = "father", required = false) String fatherName,
            @RequestParam(name = "mother", required = false) String motherName,
            @RequestParam(name = "category", required = false) String categoryName,
            @RequestParam(name = "myth", required = false) String mythName,
            @RequestParam(value = "page", defaultValue = CriteriaConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = CriteriaConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sort", defaultValue = CriteriaConstants.DEFAULT_SORT_ATTRIBUTE, required = false) String sortBy,
            @RequestParam(value = "dir", defaultValue = CriteriaConstants.DEFAULT_SORT_ORDER, required = false) String sortOrder) {
        return ResponseEntity.ok(mythCharacterService.getAllMythCharacters(
                fatherName, motherName, categoryName, mythName, pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MythCharacterResponseDto> getMythCharacterById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(mythCharacterService.getMythCharacterById(id));
    }

    @PostMapping
    public ResponseEntity<MythCharacterDto> createMythCharacter(@Valid @RequestBody CreateMythCharacterDto dto) {
        return new ResponseEntity<>(mythCharacterService.createMythCharacter(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public MythCharacterDto updateMythCharacter(@PathVariable("id") long id, @Valid @RequestBody UpdateMythCharacterDto dto) {
        return mythCharacterService.updateMythCharacter(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMythCharacter(@PathVariable(name = "id") long id) {
        mythCharacterService.deleteMythCharacter(id);
        return new ResponseEntity<>(ResponseMessages.MYTH_CHARACTER_DELETED, HttpStatus.OK);
    }
}