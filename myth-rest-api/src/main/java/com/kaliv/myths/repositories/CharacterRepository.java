package com.kaliv.myths.repositories;

import com.kaliv.myths.entities.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Long> {
}
