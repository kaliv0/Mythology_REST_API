package com.kaliv.myths.common.image;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.imgscalr.Scalr;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.constant.ImageContentType;
import com.kaliv.myths.constant.params.Args;
import com.kaliv.myths.dto.imageDtos.PaintingImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.StatueImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.UploadImageResponseDto;
import com.kaliv.myths.entity.artefacts.images.*;
import com.kaliv.myths.exception.invalidInput.InvalidImageInputException;
import com.kaliv.myths.exception.invalidInput.UnsupportedImageContentTypeException;

import lombok.SneakyThrows;

@Component
public class ImageHandler {
    public StatueImage prepareStatueImage(MultipartFile file) throws IOException {
        return StatueImage.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageCompressor.compressImage(file.getBytes()))
                .build();
    }

    public SmallStatueImage prepareSmallStatueImage(MultipartFile file) throws IOException {
        byte[] resizedFileByteArray = resizeImage(file);
        String resizedFileName = prepareResizedFileName(file.getOriginalFilename());
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

    public SmallPaintingImage prepareSmallPaintingImage(MultipartFile file) throws IOException {
        byte[] resizedFileByteArray = resizeImage(file);
        String resizedFileName = prepareResizedFileName(file.getOriginalFilename());
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

    private static byte[] resizeImage(MultipartFile file) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage outputImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Args.WIDTH);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String type = mapMediaType(file);
        ImageIO.write(outputImage, type, outputStream);
        return outputStream.toByteArray();
    }

    public static String prepareResizedFileName(String fileName) {
        StringBuilder sb = new StringBuilder();
        if (fileName == null || fileName.length() == 0) {
            throw new InvalidImageInputException();
        }

        int endIndex = fileName.lastIndexOf(Args.EXTENSION_SEPARATOR);
        if (endIndex == -1) {
            throw new InvalidImageInputException();
        }
        return sb.append(fileName, 0, endIndex)
                .append(Args.RESIZED_SUFFIX)
                .append(fileName, endIndex, fileName.length())
                .toString();
    }

    private static String mapMediaType(MultipartFile file) {
        if (file.getContentType() == null) {
            throw new InvalidImageInputException();
        }
        String type = file.getContentType();
        if (type.equals(MediaType.IMAGE_JPEG_VALUE)) {
            return ImageContentType.JPEG;
        }
        if (type.equals(MediaType.IMAGE_PNG_VALUE)) {
            return ImageContentType.PNG;
        }
        if (type.equals(MediaType.IMAGE_GIF_VALUE)) {
            return ImageContentType.GIF;
        }
        throw new UnsupportedImageContentTypeException();
    }
}
