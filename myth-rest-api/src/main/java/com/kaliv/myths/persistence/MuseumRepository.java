package com.kaliv.myths.persistence;

import org.springframework.data.repository.CrudRepository;

import com.kaliv.myths.entity.artefacts.Museum;

public interface MuseumRepository extends CrudRepository<Museum, Long> {
    boolean existsByName(String name);
}
