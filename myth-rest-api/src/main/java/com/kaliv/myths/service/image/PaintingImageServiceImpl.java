package com.kaliv.myths.service.image;

import javax.transaction.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.imageDtos.ImageDetailsDto;
import com.kaliv.myths.dto.imageDtos.PaginatedImageResponseDto;
import com.kaliv.myths.dto.imageDtos.UploadImageResponseDto;
import com.kaliv.myths.entity.artefacts.images.PaintingImage;
import com.kaliv.myths.entity.artefacts.images.SmallPaintingImage;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.persistence.PaintingImageRepository;
import com.kaliv.myths.persistence.SmallPaintingImageRepository;
import com.kaliv.myths.util.image.ImageBuilder;
import com.kaliv.myths.util.image.ImageResizeHandler;

@Service
@Qualifier("paintingImageService")
public class PaintingImageServiceImpl implements ImageService {
    private final PaintingImageRepository paintingImageRepository;
    private final SmallPaintingImageRepository smallPaintingImageRepository;
    private final ImageBuilder imageBuilder;

    @Autowired
    public PaintingImageServiceImpl(PaintingImageRepository paintingImageRepository,
                                    SmallPaintingImageRepository smallPaintingImageRepository,
                                    ImageBuilder imageBuilder) {
        this.paintingImageRepository = paintingImageRepository;
        this.smallPaintingImageRepository = smallPaintingImageRepository;
        this.imageBuilder = imageBuilder;
    }

    @Override
    public PaginatedImageResponseDto getAllImages(int pageNumber,
                                                  int pageSize,
                                                  String sortBy,
                                                  String sortOrder) {
        Sort sortCriteria = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortCriteria);
        Page<SmallPaintingImage> smallPaintingImages = smallPaintingImageRepository.findAll(pageable);

        List<ImageDetailsDto> content = smallPaintingImages
                .getContent().stream()
                .map(imageBuilder::getPaintingImageDetails)
                .collect(Collectors.toList());

        PaginatedImageResponseDto paintingImageResponseDto = new PaginatedImageResponseDto();
        paintingImageResponseDto.setContent(content);
        paintingImageResponseDto.setPageNumber(smallPaintingImages.getNumber());
        paintingImageResponseDto.setPageSize(smallPaintingImages.getSize());
        paintingImageResponseDto.setTotalElements(smallPaintingImages.getTotalElements());
        paintingImageResponseDto.setTotalPages(smallPaintingImages.getTotalPages());
        paintingImageResponseDto.setLast(smallPaintingImages.isLast());

        return paintingImageResponseDto;
    }

    public UploadImageResponseDto uploadImage(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String resizedFileName = ImageResizeHandler.prepareResizedFileName(file.getOriginalFilename());
        if (paintingImageRepository.existsByName(originalFilename)
                || smallPaintingImageRepository.existsByName(resizedFileName)) {
            throw new ResourceWithGivenValuesExistsException(Sources.IMAGE, Fields.NAME, originalFilename);
        }
        smallPaintingImageRepository.save(imageBuilder.prepareSmallPaintingImage(file, resizedFileName));
        PaintingImage savedImage = paintingImageRepository.save(imageBuilder.preparePaintingImage(file));
        return imageBuilder.getUploadImageResponseDto(savedImage.getId(), ResponseMessages.IMAGE_UPLOADED, savedImage.getName());
    }

    @Transactional
    public ImageDetailsDto getImageDetails(String name) {
        PaintingImage paintingImageInDb = paintingImageRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(Sources.IMAGE));
        return imageBuilder.getPaintingImageDetails(paintingImageInDb);
    }

    @Transactional
    public ImageDetailsDto getSmallImageDetails(String name) {
        SmallPaintingImage smallPaintingImageInDb = smallPaintingImageRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(Sources.IMAGE));
        return imageBuilder.getPaintingImageDetails(smallPaintingImageInDb);
    }

    @Override
    public void deleteImage(String name) {
        PaintingImage paintingImageInDb = paintingImageRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(Sources.IMAGE));
        paintingImageRepository.delete(paintingImageInDb);

        String resizedFileName = ImageResizeHandler.prepareResizedFileName(name);
        SmallPaintingImage smallPaintingImageInDb = smallPaintingImageRepository.findByName(resizedFileName)
                .orElseThrow(() -> new ResourceNotFoundException(Sources.IMAGE));
        smallPaintingImageRepository.delete(smallPaintingImageInDb);
    }
}

