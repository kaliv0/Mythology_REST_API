package com.kaliv.myths.util.image;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.dto.imageDtos.PaintingImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.StatueImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.UploadImageResponseDto;
import com.kaliv.myths.entity.artefacts.images.*;

import lombok.SneakyThrows;

@Component
public class ImageBuilder {
    public StatueImage prepareStatueImage(MultipartFile file) throws IOException {
        return StatueImage.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageCompressor.compressImage(file.getBytes()))
                .build();
    }

    public SmallStatueImage prepareSmallStatueImage(MultipartFile file, String resizedFileName) throws IOException {
        byte[] resizedFileByteArray = ImageResizeHandler.resizeImage(file);
        return SmallStatueImage.builder()
                .name(resizedFileName)
                .type(file.getContentType())
                .imageData(ImageCompressor.compressImage(resizedFileByteArray))
                .build();
    }

    public PaintingImage preparePaintingImage(MultipartFile file) throws IOException {
        return PaintingImage.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageCompressor.compressImage(file.getBytes()))
                .build();
    }

    public SmallPaintingImage prepareSmallPaintingImage(MultipartFile file, String resizedFileName) throws IOException {
        byte[] resizedFileByteArray = ImageResizeHandler.resizeImage(file);
        return SmallPaintingImage.builder()
                .name(resizedFileName)
                .type(file.getContentType())
                .imageData(ImageCompressor.compressImage(resizedFileByteArray))
                .build();
    }

    @SneakyThrows
    public StatueImageDetailsDto getStatueImageDetails(ArtImage statueImageInDb) {
        return StatueImageDetailsDto.builder()
                .id(statueImageInDb.getId())
                .name(statueImageInDb.getName())
                .type(statueImageInDb.getType())
                .imageData(ImageCompressor.decompressImage(statueImageInDb.getImageData()))
                .statueId(statueImageInDb.getId())
                .build();
    }

    @SneakyThrows
    public PaintingImageDetailsDto getPaintingImageDetails(ArtImage paintingImageInDb) {
        return PaintingImageDetailsDto.builder()
                .id(paintingImageInDb.getId())
                .name(paintingImageInDb.getName())
                .type(paintingImageInDb.getType())
                .imageData(ImageCompressor.decompressImage(paintingImageInDb.getImageData()))
                .paintingId(paintingImageInDb.getId())
                .build();
    }

    public UploadImageResponseDto getUploadImageResponseDto(long id, String message, String fileName) {
        return UploadImageResponseDto.builder()
                .id(id)
                .message(message)
                .name(fileName)
                .build();
    }
}
