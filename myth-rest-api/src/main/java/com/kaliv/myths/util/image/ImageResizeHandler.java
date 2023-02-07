package com.kaliv.myths.util.image;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.imgscalr.Scalr;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.constant.ImageContentType;
import com.kaliv.myths.constant.params.Args;
import com.kaliv.myths.exception.invalidInput.InvalidImageInputException;

public class ImageResizeHandler {
    public static byte[] resizeImage(MultipartFile file) throws IOException {
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

    public static String mapMediaType(MultipartFile file) {
        if (file.getContentType() == null) {
            throw new InvalidImageInputException();
        }
        String type = file.getContentType();
        String result = null;
        if (type.equals(MediaType.IMAGE_JPEG_VALUE)) {
            result = ImageContentType.JPEG;
        }
        if (type.equals(MediaType.IMAGE_PNG_VALUE)) {
            result = ImageContentType.PNG;
        }
        if (type.equals(MediaType.IMAGE_GIF_VALUE)) {
            result = ImageContentType.GIF;
        }
        return result;
    }
}
