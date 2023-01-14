package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.artefacts.Music;

public interface MusicRepository extends JpaRepository<Music, Long> {
    boolean existsByName(String name);
}

