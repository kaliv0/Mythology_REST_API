package com.kaliv.myths.controller;

import javax.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.paintingDtos.CreatePaintingDto;
import com.kaliv.myths.dto.paintingDtos.PaintingDto;
import com.kaliv.myths.dto.paintingDtos.PaintingResponseDto;
import com.kaliv.myths.dto.paintingDtos.UpdatePaintingDto;
import com.kaliv.myths.service.painting.PaintingService;

@RestController
@RequestMapping("/api/v1/paintings")
public class PaintingController {

    private final PaintingService paintingService;

    public PaintingController(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    @GetMapping
    public ResponseEntity<List<PaintingResponseDto>> getAllPaintings() {
        return ResponseEntity.ok(paintingService.getAllPaintings());
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