package com.kaliv.myths.persistence;

import com.kaliv.myths.model.artefacts.Painting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaintingRepository extends JpaRepository<Painting, Long> {
}
