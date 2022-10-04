package com.kaliv.myths.repositories;

import com.kaliv.myths.entities.artefacts.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
