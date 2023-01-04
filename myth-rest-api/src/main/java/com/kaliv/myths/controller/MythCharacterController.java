package com.kaliv.myths.controller;

import javax.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.mythCharacterDtos.CreateMythCharacterDto;
import com.kaliv.myths.dto.mythCharacterDtos.MythCharacterDto;
import com.kaliv.myths.dto.mythCharacterDtos.MythCharacterResponseDto;
import com.kaliv.myths.dto.mythCharacterDtos.UpdateMythCharacterDto;
import com.kaliv.myths.service.mythCharacter.MythCharacterService;

@RestController
@RequestMapping("/api/v1/myth-characters")
public class MythCharacterController {

    private final MythCharacterService mythCharacterService;

    public MythCharacterController(MythCharacterService mythCharacterService) {
        this.mythCharacterService = mythCharacterService;
    }

    @GetMapping
    public ResponseEntity<List<MythCharacterResponseDto>> getAllMythCharacters() {
        return ResponseEntity.ok(mythCharacterService.getAllMythCharacters());
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