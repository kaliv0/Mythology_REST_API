package com.kaliv.myths.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.kaliv.myths.entity.artefacts.images.SmallStatueImage;

public interface SmallStatueImageRepository extends JpaRepository<SmallStatueImage, Long>,
        QuerydslPredicateExecutor<SmallStatueImage> {
    Optional<SmallStatueImage> findByName(String name);

    boolean existsByName(String name);
}