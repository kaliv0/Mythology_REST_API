package com.kaliv.myths.controller;

import javax.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.poemDtos.CreatePoemDto;
import com.kaliv.myths.dto.poemDtos.PoemDto;
import com.kaliv.myths.dto.poemDtos.PoemResponseDto;
import com.kaliv.myths.dto.poemDtos.UpdatePoemDto;
import com.kaliv.myths.service.poem.PoemService;

@RestController
@RequestMapping("/api/v1/poems")
public class PoemController {

    private final PoemService poemService;

    public PoemController(PoemService poemService) {
        this.poemService = poemService;
    }

    @GetMapping
    public ResponseEntity<List<PoemResponseDto>> getAllPoems() {
        return ResponseEntity.ok(poemService.getAllPoems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PoemResponseDto> getPoemById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(poemService.getPoemById(id));
    }

    @PostMapping
    public ResponseEntity<PoemDto> createPoem(@Valid @RequestBody CreatePoemDto dto) {
        return new ResponseEntity<>(poemService.createPoem(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public PoemDto updatePoem(@PathVariable("id") long id, @Valid @RequestBody UpdatePoemDto dto) {
        return poemService.updatePoem(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePoem(@PathVariable(name = "id") long id) {
        poemService.deletePoem(id);
        return new ResponseEntity<>(ResponseMessages.POEM_DELETED, HttpStatus.OK);
    }
}