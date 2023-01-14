package com.kaliv.myths.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.artefacts.images.Image;
import com.kaliv.myths.entity.artefacts.images.StatueImage;

public interface StatueImageRepository extends JpaRepository<StatueImage, Long> {
    //    boolean existsByName(String name);
    Optional<StatueImage> findByName(String name);
}
