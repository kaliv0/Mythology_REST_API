package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.MythCharacter;

public interface MythCharacterRepository extends JpaRepository<MythCharacter, Long> {
    boolean existsByName(String name);
}
