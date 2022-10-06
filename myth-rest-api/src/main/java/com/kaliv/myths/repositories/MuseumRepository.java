package com.kaliv.myths.repositories;

import com.kaliv.myths.entities.artefacts.Museum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MuseumRepository extends JpaRepository<Museum, Long> {
}
