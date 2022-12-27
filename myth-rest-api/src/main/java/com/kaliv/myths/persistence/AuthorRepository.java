package com.kaliv.myths.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.artefacts.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);

}
