package com.kaliv.myths.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.constant.ArtworkType;
import com.kaliv.myths.dto.imageDtos.ImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.UploadImageResponseDto;
import com.kaliv.myths.service.image.ImageService;

@RestController
@RequestMapping("/api/v1/images/{artwork-type}")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(path = "/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadImageResponseDto> uploadImage(
            @PathVariable("artwork-type") ArtworkType artworkType,
            @RequestParam("image") MultipartFile file)
            throws Exception {
        return ResponseEntity.ok(imageService.uploadImage(artworkType, file));
    }

    @GetMapping("/download/info/{name}")
    public ResponseEntity<ImageDetailsDto> getImageDetails(
            @PathVariable("artwork-type") ArtworkType artworkType,
            @PathVariable("name") String name)
            throws Exception {
        return ResponseEntity.ok(imageService.getImageDetails(artworkType, name));
    }

    @GetMapping("/download/{name}")
    public ResponseEntity<byte[]> getImageByName(
            @PathVariable("artwork-type") ArtworkType artworkType,
            @PathVariable("name") String name)
            throws Exception {
        ImageDetailsDto imageInDb = imageService.getImageDetails(artworkType, name);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(imageInDb.getType()))
                .body(imageInDb.getImageData());
    }
}