package com.kaliv.myths.persistence;

import org.springframework.data.repository.CrudRepository;

import com.kaliv.myths.entity.artefacts.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    boolean existsByName(String name);
}
