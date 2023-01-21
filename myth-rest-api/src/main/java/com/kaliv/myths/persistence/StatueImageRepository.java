package com.kaliv.myths.persistence;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.kaliv.myths.entity.artefacts.images.StatueImage;

public interface StatueImageRepository extends CrudRepository<StatueImage, Long> {
    Optional<StatueImage> findByName(String name);
}
