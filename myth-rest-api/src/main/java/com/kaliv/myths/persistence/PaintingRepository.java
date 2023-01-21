package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.artefacts.Painting;

public interface PaintingRepository extends JpaRepository<Painting, Long> {
    boolean existsByName(String name);
}
