package com.kaliv.myths.controller;

import java.io.IOException;
import java.util.zip.DataFormatException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.constant.CriteriaConstants;
import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.imageDtos.ImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.PaginatedImageResponseDto;
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

    @GetMapping
    public ResponseEntity<PaginatedImageResponseDto> getAllStatueImages(
            @RequestParam(value = "page", defaultValue = CriteriaConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = CriteriaConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sort", defaultValue = CriteriaConstants.DEFAULT_SORT_ATTRIBUTE, required = false) String sortBy,
            @RequestParam(value = "dir", defaultValue = CriteriaConstants.DEFAULT_SORT_ORDER, required = false) String sortOrder)
            throws DataFormatException, IOException {
        return ResponseEntity.ok(statueImageService.getAllStatueImages(pageNumber, pageSize, sortBy, sortOrder));
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

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteStatueImage(@PathVariable(name = "name") String name) {
        statueImageService.deleteStatueImage(name);
        return new ResponseEntity<>(ResponseMessages.IMAGE_DELETED, HttpStatus.OK);
    }
}