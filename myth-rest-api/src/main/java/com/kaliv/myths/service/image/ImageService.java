package com.kaliv.myths.service.image;

import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.constant.ArtworkTypes;
import com.kaliv.myths.dto.imageDtos.ImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.UploadImageResponseDto;

public interface ImageService {
    UploadImageResponseDto uploadImage(ArtworkTypes artworkType, MultipartFile file) throws Exception;

    ImageDetailsDto getImageDetails(ArtworkTypes artworkType, String name) throws Exception;
}
