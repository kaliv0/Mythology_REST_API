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
import com.kaliv.myths.entity.artefacts.images.SmallStatueImage;
import com.kaliv.myths.entity.artefacts.images.StatueImage;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.persistence.SmallStatueImageRepository;
import com.kaliv.myths.persistence.StatueImageRepository;
import com.kaliv.myths.util.image.ImageBuilder;
import com.kaliv.myths.util.image.ImageResizeHandler;

@Service
@Qualifier("statueImageServiceImpl")
public class StatueImageServiceImpl implements ImageService {
    private final StatueImageRepository statueImageRepository;
    private final SmallStatueImageRepository smallStatueImageRepository;
    private final ImageBuilder imageBuilder;

    @Autowired
    public StatueImageServiceImpl(StatueImageRepository statueImageRepository,
                                  SmallStatueImageRepository smallStatueImageRepository,
                                  ImageBuilder imageBuilder) {
        this.statueImageRepository = statueImageRepository;
        this.smallStatueImageRepository = smallStatueImageRepository;
        this.imageBuilder = imageBuilder;
    }

    @Override
    public PaginatedImageResponseDto getAllImages(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sortCriteria = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortCriteria);
        Page<SmallStatueImage> smallStatueImages = smallStatueImageRepository.findAll(pageable);

        List<ImageDetailsDto> content = smallStatueImages.getContent().stream()
                .map(imageBuilder::getStatueImageDetails).collect(Collectors.toList());

        PaginatedImageResponseDto statueImageResponseDto = new PaginatedImageResponseDto();
        statueImageResponseDto.setContent(content);
        statueImageResponseDto.setPageNumber(smallStatueImages.getNumber());
        statueImageResponseDto.setPageSize(smallStatueImages.getSize());
        statueImageResponseDto.setTotalElements(smallStatueImages.getTotalElements());
        statueImageResponseDto.setTotalPages(smallStatueImages.getTotalPages());
        statueImageResponseDto.setLast(smallStatueImages.isLast());

        return statueImageResponseDto;
    }

    public UploadImageResponseDto uploadImage(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String resizedFileName = ImageResizeHandler.prepareResizedFileName(file.getOriginalFilename());
        if (statueImageRepository.existsByName(originalFilename)
                || smallStatueImageRepository.existsByName(resizedFileName)) {
            throw new ResourceWithGivenValuesExistsException(Sources.IMAGE, Fields.NAME, originalFilename);
        }
        StatueImage savedImage = statueImageRepository.save(imageBuilder.prepareStatueImage(file));
        smallStatueImageRepository.save(imageBuilder.prepareSmallStatueImage(file, resizedFileName));
        return imageBuilder.getUploadImageResponseDto(savedImage.getId(), ResponseMessages.IMAGE_UPLOADED, savedImage.getName());
    }

    @Transactional
    public ImageDetailsDto getImageDetails(String name) {
        StatueImage statueImageInDb = statueImageRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(Sources.IMAGE));
        return imageBuilder.getStatueImageDetails(statueImageInDb);
    }

    @Transactional
    public ImageDetailsDto getSmallImageDetails(String name) {
        SmallStatueImage smallStatueImageInDb = smallStatueImageRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(Sources.IMAGE));
        return imageBuilder.getStatueImageDetails(smallStatueImageInDb);
    }

    @Override
    public void deleteImage(String name) {
        StatueImage statueImageInDb = statueImageRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(Sources.IMAGE));
        statueImageRepository.delete(statueImageInDb);

        String resizedFileName = ImageResizeHandler.prepareResizedFileName(name);
        SmallStatueImage smallStatueImageInDb = smallStatueImageRepository.findByName(resizedFileName)
                .orElseThrow(() -> new ResourceNotFoundException(Sources.IMAGE));
        smallStatueImageRepository.delete(smallStatueImageInDb);
    }
}

