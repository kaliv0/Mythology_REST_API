package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.kaliv.myths.entity.artefacts.Painting;

public interface PaintingRepository extends JpaRepository<Painting, Long>,
        QuerydslPredicateExecutor<Painting> {
    boolean existsByName(String name);
}
