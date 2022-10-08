package com.kaliv.myths.persistence;

import com.kaliv.myths.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Long> {
}
