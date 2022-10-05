package com.kaliv.myths.controllers;

import com.kaliv.myths.common.PaginationCriteria;
import com.kaliv.myths.common.SortCriteria;
import com.kaliv.myths.constants.ResponseMessages;
import com.kaliv.myths.dtos.mythsDtos.CreateMythDto;
import com.kaliv.myths.dtos.mythsDtos.MythDto;
import com.kaliv.myths.dtos.mythsDtos.MythResponseDto;
import com.kaliv.myths.dtos.mythsDtos.UpdateMythDto;
import com.kaliv.myths.services.contracts.MythService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/myths")
public class MythController {

    private final MythService mythService;

    public MythController(MythService mythService) {
        this.mythService = mythService;
    }

    @GetMapping
    public ResponseEntity<MythResponseDto> getAllMyths(PaginationCriteria paginationCriteria,
                                                       SortCriteria sortCriteria) {
        return ResponseEntity.ok(mythService.getAllMyths(paginationCriteria, sortCriteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MythDto> getMythById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(mythService.getMythById(id));
    }

    @PostMapping
    public ResponseEntity<MythDto> createMyth(@Valid @RequestBody CreateMythDto dto) {
        return new ResponseEntity<>(mythService.createMyth(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public MythDto update(@PathVariable("id") Long id, @Valid @RequestBody UpdateMythDto dto) {
        return mythService.updateMyth(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMyth(@PathVariable(name = "id") long id) {
        mythService.deleteMyth(id);
        return new ResponseEntity<>(ResponseMessages.MYTH_DELETED, HttpStatus.OK);
    }
}
