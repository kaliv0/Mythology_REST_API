package com.kaliv.myths.persistence;

import com.kaliv.myths.entity.artefacts.Painting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaintingRepository extends JpaRepository<Painting, Long> {
}
