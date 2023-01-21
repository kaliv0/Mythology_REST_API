package com.kaliv.myths.persistence;

import org.springframework.data.repository.CrudRepository;

import com.kaliv.myths.entity.MythCharacter;

public interface MythCharacterRepository extends CrudRepository<MythCharacter, Long> {
    boolean existsByName(String name);
}
