package com.kaliv.myths.service.image;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.common.utils.ImageHandler;
import com.kaliv.myths.entity.artefacts.images.StatueImage;
import com.kaliv.myths.persistence.StatueImageRepository;

@Service
public class ImageServiceImpl implements ImageService {

    private StatueImageRepository statueImageRepository;

    public ImageServiceImpl(StatueImageRepository statueImageRepository) {
        this.statueImageRepository = statueImageRepository;
    }

    public Long uploadImage(MultipartFile file) throws IOException {

        var savedImage = statueImageRepository.save(StatueImage.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageHandler.compressImage(file.getBytes())).build());

        return savedImage.getId();

    }

//    @Transactional
//    public StatueImage getInfoByImageByName(String name) {
//        Optional<StatueImage> dbImage = statueImageRepository.findByName(name);
//
//        return StatueImage.builder()
//                .name(dbImage.get().getName())
//                .type(dbImage.get().getType())
//                .imageData(ImageUtil.decompressImage(dbImage.get().getImageData())).build();
//
//    }
//
//    @Transactional
//    public byte[] getImage(String name) {
//        Optional<StatueImage> dbImage = statueImageRepository.findByName(name);
//        byte[] image = ImageHandler.decompressImage(dbImage.get().getImageData());
//        return image;
//    }
}
