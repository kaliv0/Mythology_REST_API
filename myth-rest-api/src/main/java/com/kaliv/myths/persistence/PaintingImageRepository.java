package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.artefacts.images.PaintingImage;

public interface PaintingImageRepository extends JpaRepository<PaintingImage, Long> {
    boolean existsByName(String name);
}
