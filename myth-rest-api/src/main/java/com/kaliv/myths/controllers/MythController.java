package com.kaliv.myths.controllers;

import com.kaliv.myths.constants.ResponseMessages;
import com.kaliv.myths.dtos.mythsDtos.CreateMythDto;
import com.kaliv.myths.dtos.mythsDtos.MythDto;
import com.kaliv.myths.services.contracts.MythService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/myths")
public class MythController {

    private final MythService mythService;

    public MythController(MythService mythService) {
        this.mythService = mythService;
    }

    @GetMapping
    public ResponseEntity<List<MythDto>> getAllMyths() {
        return ResponseEntity.ok(mythService.getAllMyths());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MythDto> getMythById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(mythService.getMythById(id));
    }

    @PostMapping
    public ResponseEntity<MythDto> create(@Valid @RequestBody CreateMythDto dto) {
        return ResponseEntity.ok(mythService.createMyth(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMyth(@PathVariable(name = "id") long id) {
        mythService.deleteMyth(id);
        return new ResponseEntity<>(ResponseMessages.MYTH_DELETED, HttpStatus.OK);
    }
}
