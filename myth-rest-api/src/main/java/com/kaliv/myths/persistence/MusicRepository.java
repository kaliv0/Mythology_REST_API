package com.kaliv.myths.persistence;

import org.springframework.data.repository.CrudRepository;

import com.kaliv.myths.entity.artefacts.Music;

public interface MusicRepository extends CrudRepository<Music, Long> {
    boolean existsByName(String name);
}

