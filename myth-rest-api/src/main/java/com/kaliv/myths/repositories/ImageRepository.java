package com.kaliv.myths.repositories;

import com.kaliv.myths.entities.artefacts.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
