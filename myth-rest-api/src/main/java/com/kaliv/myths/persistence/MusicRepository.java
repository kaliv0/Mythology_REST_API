package com.kaliv.myths.persistence;

import com.kaliv.myths.entity.artefacts.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
}
