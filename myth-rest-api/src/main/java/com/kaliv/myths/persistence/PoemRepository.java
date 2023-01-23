package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.kaliv.myths.entity.artefacts.Poem;

public interface PoemRepository extends JpaRepository<Poem, Long>,
        QuerydslPredicateExecutor<Poem> {
    boolean existsByName(String name);
}
