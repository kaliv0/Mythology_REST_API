package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.kaliv.myths.entity.artefacts.Author;

public interface AuthorRepository extends JpaRepository<Author, Long>,
        QuerydslPredicateExecutor<Author> {
    boolean existsByName(String name);
}
