package com.kaliv.myths.persistence;

import com.kaliv.myths.model.artefacts.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
}
