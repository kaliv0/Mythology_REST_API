package com.kaliv.myths.controller;

import java.io.IOException;
import java.util.zip.DataFormatException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.util.validator.file.ValidFile;
import com.kaliv.myths.constant.CriteriaConstants;
import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.imageDtos.ImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.PaginatedImageResponseDto;
import com.kaliv.myths.dto.imageDtos.UploadImageResponseDto;
import com.kaliv.myths.service.image.ImageService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Painting images")
@Validated
@RestController
@RequestMapping("/api/v1/images/paintings")
public class PaintingImageController {

    private final ImageService paintingImageService;

    public PaintingImageController(@Qualifier("paintingImageService") ImageService imageService) {
        this.paintingImageService = imageService;
    }

    @GetMapping
    public ResponseEntity<PaginatedImageResponseDto> getAllPaintingImages(
            @RequestParam(value = "page", defaultValue = CriteriaConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = CriteriaConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sort", defaultValue = CriteriaConstants.DEFAULT_SORT_ATTRIBUTE, required = false) String sortBy,
            @RequestParam(value = "dir", defaultValue = CriteriaConstants.DEFAULT_SORT_ORDER, required = false) String sortOrder)
            throws DataFormatException, IOException {
        return ResponseEntity.ok(paintingImageService.getAllImages(pageNumber, pageSize, sortBy, sortOrder));
    }

    @PostMapping(path = "/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadImageResponseDto> uploadPaintingImage(
            @ValidFile @RequestParam("image") MultipartFile file) throws Exception {
        return ResponseEntity.ok(paintingImageService.uploadImage(file));
    }

    @GetMapping("/download/info/{name}")
    public ResponseEntity<ImageDetailsDto> getPaintingImageDetails(@PathVariable("name") String name)
            throws Exception {
        return ResponseEntity.ok(paintingImageService.getImageDetails(name));
    }

    @GetMapping("/tiny-images/download/info/{name}")
    public ResponseEntity<ImageDetailsDto> getSmallPaintingImageDetails(@PathVariable("name") String name)
            throws Exception {
        return ResponseEntity.ok(paintingImageService.getSmallImageDetails(name));
    }

    @GetMapping("/download/{name}")
    public ResponseEntity<byte[]> getPaintingImageByName(@PathVariable("name") String name)
            throws Exception {
        ImageDetailsDto imageInDb = paintingImageService.getImageDetails(name);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(imageInDb.getType()))
                .body(imageInDb.getImageData());
    }

    @GetMapping("/tiny-images/download/{name}")
    public ResponseEntity<byte[]> getSmallPaintingImageByName(@PathVariable("name") String name)
            throws Exception {
        ImageDetailsDto imageInDb = paintingImageService.getSmallImageDetails(name);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(imageInDb.getType()))
                .body(imageInDb.getImageData());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deletePaintingImage(@PathVariable(name = "name") String name) {
        paintingImageService.deleteImage(name);
        return new ResponseEntity<>(ResponseMessages.IMAGE_DELETED, HttpStatus.OK);
    }
}