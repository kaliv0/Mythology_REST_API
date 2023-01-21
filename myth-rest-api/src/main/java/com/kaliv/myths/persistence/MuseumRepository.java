package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.artefacts.Museum;

public interface MuseumRepository extends JpaRepository<Museum, Long> {
    boolean existsByName(String name);
}
