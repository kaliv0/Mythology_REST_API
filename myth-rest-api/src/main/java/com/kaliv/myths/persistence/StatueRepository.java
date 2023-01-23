package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.kaliv.myths.entity.artefacts.Statue;

public interface StatueRepository extends JpaRepository<Statue, Long>,
        QuerydslPredicateExecutor<Statue> {
    boolean existsByName(String name);
}
