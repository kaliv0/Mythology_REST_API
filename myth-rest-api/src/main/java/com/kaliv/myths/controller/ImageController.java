package com.kaliv.myths.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.service.image.ImageService;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        return ResponseEntity.ok(imageService.uploadImage(file));
    }

//    @GetMapping("/info/{name}")
//    public ResponseEntity<?> getImageInfoByName(@PathVariable("name") String name) {
//        ImageData image = imageDataService.getInfoByImageByName(name);
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(image);
//    }
//
//    @GetMapping("/{name}")
//    public ResponseEntity<?> getImageByName(@PathVariable("name") String name) {
//        byte[] image = imageDataService.getImage(name);
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.valueOf("image/png"))
//                .body(image);
//    }
}
