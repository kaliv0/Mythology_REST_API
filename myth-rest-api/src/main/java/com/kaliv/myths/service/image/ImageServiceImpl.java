package com.kaliv.myths.service.image;

import javax.transaction.Transactional;

import java.io.IOException;
import java.util.zip.DataFormatException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.constant.ArtworkType;
import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.imageDtos.ImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.UploadImageResponseDto;
import com.kaliv.myths.entity.artefacts.images.PaintingImage;
import com.kaliv.myths.entity.artefacts.images.StatueImage;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.invalidInput.InvalidArtworkTypeException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.mapper.ImageBuilder;
import com.kaliv.myths.persistence.PaintingImageRepository;
import com.kaliv.myths.persistence.StatueImageRepository;

@Service
public class ImageServiceImpl implements ImageService {

    private final StatueImageRepository statueImageRepository;
    private final PaintingImageRepository paintingImageRepository;
    private final ImageBuilder imageBuilder;


    public ImageServiceImpl(StatueImageRepository statueImageRepository,
                            PaintingImageRepository paintingImageRepository,
                            ImageBuilder imageBuilder) {
        this.statueImageRepository = statueImageRepository;
        this.paintingImageRepository = paintingImageRepository;
        this.imageBuilder = imageBuilder;
    }

    public UploadImageResponseDto uploadImage(ArtworkType artworkType, MultipartFile file)
            throws InvalidArtworkTypeException, IOException {
        PaintingImage savedImage = null;
        if (artworkType.equals(ArtworkType.STATUE)) {
            if (statueImageRepository.existsByName(file.getOriginalFilename())) {
                throw new ResourceWithGivenValuesExistsException(Sources.IMAGE, Fields.NAME, file.getOriginalFilename());
            }
            statueImageRepository.save(imageBuilder.getStatueImage(file));
        } else if (artworkType.equals(ArtworkType.PAINTING)) {
            if (paintingImageRepository.existsByName(file.getOriginalFilename())) {
                throw new ResourceWithGivenValuesExistsException(Sources.IMAGE, Fields.NAME, file.getOriginalFilename());
            }
            savedImage = paintingImageRepository.save(imageBuilder.getPaintingImage(file));
        } else {
            throw new InvalidArtworkTypeException();
        }
        return imageBuilder.geUploadImageResponseDto(savedImage.getId(),
                ResponseMessages.IMAGE_UPLOADED, savedImage.getName());
    }

    @Transactional
    public ImageDetailsDto getImageDetails(ArtworkType artworkType, String name)
            throws InvalidArtworkTypeException, DataFormatException, IOException {
        if (artworkType.equals(ArtworkType.STATUE)) {
            StatueImage statueImageInDb = statueImageRepository.findByName(name)
                    .orElseThrow(() -> new ResourceNotFoundException(Sources.IMAGE));
            return imageBuilder.getStatueImageDetails(statueImageInDb);
        }
        if (artworkType.equals(ArtworkType.PAINTING)) {
            PaintingImage paintingImageInDb = paintingImageRepository.findByName(name)
                    .orElseThrow(() -> new ResourceNotFoundException(Sources.IMAGE));
            return imageBuilder.getPaintingImageDetails(paintingImageInDb);
        }
        throw new InvalidArtworkTypeException();
    }
}
