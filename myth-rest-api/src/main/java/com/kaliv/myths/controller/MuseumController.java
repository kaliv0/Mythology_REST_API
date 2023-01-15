package com.kaliv.myths.controller;

import javax.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.museumDtos.CreateMuseumDto;
import com.kaliv.myths.dto.museumDtos.MuseumDto;
import com.kaliv.myths.dto.museumDtos.MuseumResponseDto;
import com.kaliv.myths.dto.museumDtos.UpdateMuseumDto;
import com.kaliv.myths.service.museum.MuseumService;

@RestController
@RequestMapping("/api/v1/museums")
public class MuseumController {

    private final MuseumService museumService;

    public MuseumController(MuseumService museumService) {
        this.museumService = museumService;
    }

    @GetMapping
    public ResponseEntity<List<MuseumResponseDto>> getAllMuseums() {
        return ResponseEntity.ok(museumService.getAllMuseums());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MuseumResponseDto> getMuseumById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(museumService.getMuseumById(id));
    }

    @PostMapping
    public ResponseEntity<MuseumDto> createMuseum(@Valid @RequestBody CreateMuseumDto dto) {
        return new ResponseEntity<>(museumService.createMuseum(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public MuseumDto updateMuseum(@PathVariable("id") long id, @Valid @RequestBody UpdateMuseumDto dto) {
        return museumService.updateMuseum(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMuseum(@PathVariable(name = "id") long id) {
        museumService.deleteMuseum(id);
        return new ResponseEntity<>(ResponseMessages.MUSEUM_DELETED, HttpStatus.OK);
    }
}