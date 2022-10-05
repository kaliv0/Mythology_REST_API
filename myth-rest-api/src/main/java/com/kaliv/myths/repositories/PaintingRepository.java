package com.kaliv.myths.repositories;

import com.kaliv.myths.entities.artefacts.Painting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaintingRepository extends JpaRepository<Painting, Long> {
}
