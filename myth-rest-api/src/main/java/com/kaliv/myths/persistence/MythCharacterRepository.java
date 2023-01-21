package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.kaliv.myths.entity.MythCharacter;

public interface MythCharacterRepository extends JpaRepository<MythCharacter, Long>,
        QuerydslPredicateExecutor<MythCharacter> {
    boolean existsByName(String name);
}
