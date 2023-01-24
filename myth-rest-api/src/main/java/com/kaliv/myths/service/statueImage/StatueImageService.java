package com.kaliv.myths.service.statueImage;

import java.io.IOException;
import java.util.zip.DataFormatException;

import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.dto.imageDtos.ImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.PaginatedImageResponseDto;
import com.kaliv.myths.dto.imageDtos.UploadImageResponseDto;

public interface StatueImageService {
    PaginatedImageResponseDto getAllStatueImages(int pageNumber, int pageSize, String sortBy, String sortOrder) throws DataFormatException, IOException;

    UploadImageResponseDto uploadImage(MultipartFile file) throws Exception;

    ImageDetailsDto getImageDetails(String name) throws Exception;

    ImageDetailsDto getSmallImageDetails(String name) throws Exception;

    void deleteStatueImage(String name);
}
