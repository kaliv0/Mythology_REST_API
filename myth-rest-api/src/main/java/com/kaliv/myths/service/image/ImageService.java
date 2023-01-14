package com.kaliv.myths.service.image;

import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.constant.ArtworkType;
import com.kaliv.myths.dto.imageDtos.ImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.UploadImageResponseDto;

public interface ImageService {
    UploadImageResponseDto uploadImage(ArtworkType artworkType, MultipartFile file) throws Exception;

    ImageDetailsDto getImageDetails(ArtworkType artworkType, String name) throws Exception;
}
