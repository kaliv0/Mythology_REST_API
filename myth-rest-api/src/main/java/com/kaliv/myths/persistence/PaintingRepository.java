package com.kaliv.myths.persistence;

import org.springframework.data.repository.CrudRepository;

import com.kaliv.myths.entity.artefacts.Painting;

public interface PaintingRepository extends CrudRepository<Painting, Long> {
    boolean existsByName(String name);
}
