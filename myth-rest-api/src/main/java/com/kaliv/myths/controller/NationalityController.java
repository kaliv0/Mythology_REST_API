package com.kaliv.myths.controller;

import javax.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.nationalityDtos.CreateNationalityDto;
import com.kaliv.myths.dto.nationalityDtos.NationalityDto;
import com.kaliv.myths.dto.nationalityDtos.NationalityResponseDto;
import com.kaliv.myths.dto.nationalityDtos.UpdateNationalityDto;
import com.kaliv.myths.service.nationality.NationalityService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Nationalities")
@RestController
@RequestMapping("/api/v1/nationalities")
public class NationalityController {

    private final NationalityService nationalityService;

    public NationalityController(NationalityService nationalityService) {
        this.nationalityService = nationalityService;
    }

    @GetMapping
    public ResponseEntity<List<NationalityResponseDto>> getAllNationalities() {
        return ResponseEntity.ok(nationalityService.getAllNationalities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NationalityResponseDto> getNationalityById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(nationalityService.getNationalityById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole({'STAFF','ADMIN'})")
    public ResponseEntity<NationalityDto> createNationality(@Valid @RequestBody CreateNationalityDto dto) {
        return new ResponseEntity<>(nationalityService.createNationality(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole({'STAFF','ADMIN'})")
    public NationalityDto updateNationality(@PathVariable("id") long id, @Valid @RequestBody UpdateNationalityDto dto) {
        return nationalityService.updateNationality(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteNationality(@PathVariable(name = "id") long id) {
        nationalityService.deleteNationality(id);
        return new ResponseEntity<>(ResponseMessages.NATIONALITY_DELETED, HttpStatus.OK);
    }
}