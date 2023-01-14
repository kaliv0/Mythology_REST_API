package com.kaliv.myths.service.image;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Long uploadImage(MultipartFile file) throws IOException;
}
