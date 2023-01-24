package com.kaliv.myths.service.statueImage;

import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.dto.imageDtos.ImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.UploadImageResponseDto;

public interface StatueImageService {
    UploadImageResponseDto uploadImage(MultipartFile file) throws Exception;

    ImageDetailsDto getImageDetails(String name) throws Exception;

    ImageDetailsDto getSmallImageDetails(String name) throws Exception;
}
