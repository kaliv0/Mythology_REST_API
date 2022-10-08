package com.kaliv.myths.persistence;

import com.kaliv.myths.model.artefacts.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
