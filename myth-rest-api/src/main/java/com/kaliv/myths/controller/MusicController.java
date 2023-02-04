package com.kaliv.myths.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.CriteriaConstants;
import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.musicDtos.*;
import com.kaliv.myths.service.music.MusicService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Music")
@RestController
@RequestMapping("/api/v1/music")
public class MusicController {

    private final MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping
    public ResponseEntity<PaginatedMusicResponseDto> getAllMusic(
            @RequestParam(name = "author", required = false) String authorName,
            @RequestParam(name = "myth", required = false) String mythName,
            @RequestParam(name = "character", required = false) String characterName,
            @RequestParam(value = "page", defaultValue = CriteriaConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = CriteriaConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sort", defaultValue = CriteriaConstants.DEFAULT_SORT_ATTRIBUTE, required = false) String sortBy,
            @RequestParam(value = "dir", defaultValue = CriteriaConstants.DEFAULT_SORT_ORDER, required = false) String sortOrder) {
        return ResponseEntity.ok(musicService.getAllMusic(
                authorName, mythName, characterName, pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MusicResponseDto> getMusicById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(musicService.getMusicById(id));
    }

    @PostMapping
    public ResponseEntity<MusicDto> createMusic(@Valid @RequestBody CreateMusicDto dto) {
        return new ResponseEntity<>(musicService.createMusic(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public MusicDto updateMusic(@PathVariable("id") long id, @Valid @RequestBody UpdateMusicDto dto) {
        return musicService.updateMusic(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMusic(@PathVariable(name = "id") long id) {
        musicService.deleteMusic(id);
        return new ResponseEntity<>(ResponseMessages.MUSIC_DELETED, HttpStatus.OK);
    }
}