package com.kaliv.myths.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.artefacts.images.SmallPaintingImage;
import com.querydsl.core.BooleanBuilder;

public interface SmallPaintingImageRepository extends JpaRepository<SmallPaintingImage, Long> {
    Optional<SmallPaintingImage> findByName(String name);

    boolean existsByName(String name);

    List<SmallPaintingImage> findAll(BooleanBuilder booleanBuilder);
}

