package com.kaliv.myths.persistence;

import com.kaliv.myths.model.artefacts.Museum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MuseumRepository extends JpaRepository<Museum, Long> {
}
