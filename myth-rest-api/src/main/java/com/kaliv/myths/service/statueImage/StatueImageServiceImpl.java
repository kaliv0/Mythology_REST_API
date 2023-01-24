package com.kaliv.myths.service.statueImage;

import javax.transaction.Transactional;

import java.io.IOException;
import java.util.zip.DataFormatException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.imageDtos.ImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.UploadImageResponseDto;
import com.kaliv.myths.entity.artefacts.images.SmallStatueImage;
import com.kaliv.myths.entity.artefacts.images.StatueImage;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.invalidInput.InvalidArtworkTypeException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.common.image.ImageHandler;
import com.kaliv.myths.persistence.SmallStatueImageRepository;
import com.kaliv.myths.persistence.StatueImageRepository;

@Service
public class StatueImageServiceImpl implements StatueImageService {

    private final StatueImageRepository statueImageRepository;
    private final SmallStatueImageRepository smallStatueImageRepository;
    private final ImageHandler imageBuilder;


    public StatueImageServiceImpl(StatueImageRepository statueImageRepository,
                                  SmallStatueImageRepository smallStatueImageRepository,
                                  ImageHandler imageBuilder) {
        this.statueImageRepository = statueImageRepository;
        this.smallStatueImageRepository = smallStatueImageRepository;
        this.imageBuilder = imageBuilder;
    }

    public UploadImageResponseDto uploadImage(MultipartFile file) throws InvalidArtworkTypeException, IOException {
        String originalFilename = file.getOriginalFilename();
        if (statueImageRepository.existsByName(originalFilename)) {
            throw new ResourceWithGivenValuesExistsException(Sources.IMAGE, Fields.NAME, originalFilename);
        }
        smallStatueImageRepository.save(imageBuilder.prepareSmallStatueImage(file));
        StatueImage savedImage = statueImageRepository.save(imageBuilder.prepareStatueImage(file));
        return imageBuilder.getUploadImageResponseDto(savedImage.getId(), ResponseMessages.IMAGE_UPLOADED, savedImage.getName());
    }

    @Transactional
    public ImageDetailsDto getImageDetails(String name)
            throws InvalidArtworkTypeException, DataFormatException, IOException {
        StatueImage statueImageInDb = statueImageRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(Sources.IMAGE));
        return imageBuilder.getStatueImageDetails(statueImageInDb);
    }

    @Transactional
    public ImageDetailsDto getSmallImageDetails(String name)
            throws InvalidArtworkTypeException, DataFormatException, IOException {
        SmallStatueImage smallStatueImageInDb = smallStatueImageRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(Sources.IMAGE));
        return imageBuilder.getStatueImageDetails(smallStatueImageInDb);
    }
}

