package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.kaliv.myths.entity.artefacts.Music;

public interface MusicRepository extends JpaRepository<Music, Long>,
        QuerydslPredicateExecutor<Music> {
    boolean existsByName(String name);
}

