package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.artefacts.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByName(String name);
}
