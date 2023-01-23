package com.kaliv.myths.mapper;

import java.io.IOException;
import java.util.zip.DataFormatException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.common.ImageHandler;
import com.kaliv.myths.dto.imageDtos.PaintingImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.StatueImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.UploadImageResponseDto;
import com.kaliv.myths.entity.artefacts.images.PaintingImage;
import com.kaliv.myths.entity.artefacts.images.StatueImage;

@Component
public class ImageBuilder {
    public StatueImage getStatueImage(MultipartFile file) throws IOException {
        return StatueImage.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageHandler.compressImage(file.getBytes()))
                .build();
    }

    public PaintingImage getPaintingImage(MultipartFile file) throws IOException {
        return PaintingImage.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageHandler.compressImage(file.getBytes()))
                .build();
    }

    public StatueImageDetailsDto getStatueImageDetails(StatueImage statueImageInDb)
            throws DataFormatException, IOException {
        return StatueImageDetailsDto.builder()
                .id(statueImageInDb.getId())
                .name(statueImageInDb.getName())
                .type(statueImageInDb.getType())
                .imageData(ImageHandler.decompressImage(statueImageInDb.getImageData()))
                .statueId(statueImageInDb.getId())
                .build();
    }

    public PaintingImageDetailsDto getPaintingImageDetails(PaintingImage paintingImageInDb)
            throws DataFormatException, IOException {
        return PaintingImageDetailsDto.builder()
                .id(paintingImageInDb.getId())
                .name(paintingImageInDb.getName())
                .type(paintingImageInDb.getType())
                .imageData(ImageHandler.decompressImage(paintingImageInDb.getImageData()))
                .paintingId(paintingImageInDb.getId())
                .build();
    }

    public UploadImageResponseDto geUploadImageResponseDto(long id, String message, String fileName) {
        return UploadImageResponseDto.builder()
                .id(id)
                .message(message)
                .name(fileName)
                .build();
    }
}
