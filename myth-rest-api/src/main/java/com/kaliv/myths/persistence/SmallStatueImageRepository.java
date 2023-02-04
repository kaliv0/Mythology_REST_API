package com.kaliv.myths.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.artefacts.images.SmallStatueImage;
import com.querydsl.core.BooleanBuilder;

public interface SmallStatueImageRepository extends JpaRepository<SmallStatueImage, Long> {
    Optional<SmallStatueImage> findByName(String name);

    boolean existsByName(String name);

    List<SmallStatueImage> findAll(BooleanBuilder booleanBuilder);
}