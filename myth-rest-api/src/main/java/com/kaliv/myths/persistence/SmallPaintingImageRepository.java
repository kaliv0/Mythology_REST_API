package com.kaliv.myths.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.kaliv.myths.entity.artefacts.images.SmallPaintingImage;

public interface SmallPaintingImageRepository extends JpaRepository<SmallPaintingImage, Long>,
        QuerydslPredicateExecutor<SmallPaintingImage> {
    Optional<SmallPaintingImage> findByName(String name);

    boolean existsByName(String name);
}

