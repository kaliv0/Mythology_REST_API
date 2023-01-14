package com.kaliv.myths.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.constant.ArtworkTypes;
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
            @PathVariable("artwork-type") ArtworkTypes artworkType,
            @RequestParam("image") MultipartFile file) throws Exception {
        return ResponseEntity.ok(imageService.uploadImage(artworkType, file));
    }

    @GetMapping("/download/info/{name}")
    public ResponseEntity<ImageDetailsDto> getImageDetails(
            @PathVariable("artwork-type") ArtworkTypes artworkType,
            @PathVariable("name") String name) throws Exception {
        return ResponseEntity.ok(imageService.getImageDetails(artworkType, name));
    }

//    @GetMapping("/{name}")
//    public ResponseEntity<?> getImageByName(@PathVariable("name") String name) {
//        byte[] image = imageDataService.getImage(name);
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.valueOf("image/png"))
//                .body(image);
//    }
}
