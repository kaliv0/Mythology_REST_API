package com.kaliv.myths.repositories;

import com.kaliv.myths.entities.artefacts.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
}
