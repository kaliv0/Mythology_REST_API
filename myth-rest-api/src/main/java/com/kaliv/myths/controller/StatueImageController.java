package com.kaliv.myths.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.dto.imageDtos.ImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.UploadImageResponseDto;
import com.kaliv.myths.service.statueImage.StatueImageService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Statue images")
@RestController
@RequestMapping("/api/v1/images/statues")
public class StatueImageController {

    private final StatueImageService statueImageService;

    public StatueImageController(StatueImageService imageService) {
        this.statueImageService = imageService;
    }

    @PostMapping(path = "/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadImageResponseDto> uploadImage(@RequestParam("image") MultipartFile file)
            throws Exception {
        return ResponseEntity.ok(statueImageService.uploadImage(file));
    }

    @GetMapping("/download/info/{name}")
    public ResponseEntity<ImageDetailsDto> getImageDetails(@PathVariable("name") String name)
            throws Exception {
        return ResponseEntity.ok(statueImageService.getImageDetails(name));
    }

    @GetMapping("/tiny-images/download/info/{name}")
    public ResponseEntity<ImageDetailsDto> getSmallImageDetails(@PathVariable("name") String name)
            throws Exception {
        return ResponseEntity.ok(statueImageService.getSmallImageDetails(name));
    }

    @GetMapping("/download/{name}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable("name") String name)
            throws Exception {
        ImageDetailsDto imageInDb = statueImageService.getImageDetails(name);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(imageInDb.getType()))
                .body(imageInDb.getImageData());
    }

    @GetMapping("/tiny-images/download/{name}")
    public ResponseEntity<byte[]> getSmallImageByName(@PathVariable("name") String name)
            throws Exception {
        ImageDetailsDto imageInDb = statueImageService.getSmallImageDetails(name);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(imageInDb.getType()))
                .body(imageInDb.getImageData());
    }
}