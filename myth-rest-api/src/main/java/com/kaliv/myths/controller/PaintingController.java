package com.kaliv.myths.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.CriteriaConstants;
import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.paintingDtos.*;
import com.kaliv.myths.service.painting.PaintingService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Paintings")
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
            @RequestParam(value = "page", defaultValue = CriteriaConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = CriteriaConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sort", defaultValue = CriteriaConstants.DEFAULT_SORT_ATTRIBUTE, required = false) String sortBy,
            @RequestParam(value = "dir", defaultValue = CriteriaConstants.DEFAULT_SORT_ORDER, required = false) String sortOrder) {
        return ResponseEntity.ok(paintingService.getAllPaintings(
                authorName, mythName, museumName, characterName, pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaintingResponseDto> getPaintingById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(paintingService.getPaintingById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole({'STAFF','ADMIN'})")
    public ResponseEntity<PaintingDto> createPainting(@Valid @RequestBody CreatePaintingDto dto) {
        return new ResponseEntity<>(paintingService.createPainting(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole({'STAFF','ADMIN'})")
    public PaintingDto updatePainting(@PathVariable("id") long id, @Valid @RequestBody UpdatePaintingDto dto) {
        return paintingService.updatePainting(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePainting(@PathVariable(name = "id") long id) {
        paintingService.deletePainting(id);
        return new ResponseEntity<>(ResponseMessages.PAINTING_DELETED, HttpStatus.OK);
    }
}