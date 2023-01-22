package com.kaliv.myths.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.common.criteria.PaginationCriteria;
import com.kaliv.myths.common.criteria.SortCriteria;
import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.paintingDtos.*;
import com.kaliv.myths.service.painting.PaintingService;

@RestController
@RequestMapping("/api/v1/paintings")
public class PaintingController {

    private final PaintingService paintingService;

    public PaintingController(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    @GetMapping
    public ResponseEntity<PaginatedPaintingResponseDto> getAllPaintings(
            @RequestParam(name = "author", required = false) String authorName,
            @RequestParam(name = "myth", required = false) String mythName,
            @RequestParam(name = "museum", required = false) String museumName,
            @RequestParam(name = "character", required = false) String characterName,
            PaginationCriteria paginationCriteria,
            SortCriteria sortCriteria) {
        return ResponseEntity.ok(paintingService.getAllPaintings(
                authorName, mythName, museumName, characterName, paginationCriteria, sortCriteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaintingResponseDto> getPaintingById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(paintingService.getPaintingById(id));
    }

    @PostMapping
    public ResponseEntity<PaintingDto> createPainting(@Valid @RequestBody CreatePaintingDto dto) {
        return new ResponseEntity<>(paintingService.createPainting(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public PaintingDto updatePainting(@PathVariable("id") long id, @Valid @RequestBody UpdatePaintingDto dto) {
        return paintingService.updatePainting(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePainting(@PathVariable(name = "id") long id) {
        paintingService.deletePainting(id);
        return new ResponseEntity<>(ResponseMessages.PAINTING_DELETED, HttpStatus.OK);
    }
}