package com.kaliv.myths.persistence;

import com.kaliv.myths.model.MythCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<MythCharacter, Long> {
}
