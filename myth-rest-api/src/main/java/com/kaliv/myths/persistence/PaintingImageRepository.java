package com.kaliv.myths.persistence;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.kaliv.myths.entity.artefacts.images.PaintingImage;

public interface PaintingImageRepository extends CrudRepository<PaintingImage, Long> {
    Optional<PaintingImage> findByName(String name);
}
