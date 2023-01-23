package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.kaliv.myths.entity.Myth;

public interface MythRepository extends JpaRepository<Myth, Long>,
        QuerydslPredicateExecutor<Myth> {
    boolean existsByName(String name);
}
