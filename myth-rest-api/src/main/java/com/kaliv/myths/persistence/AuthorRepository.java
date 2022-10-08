package com.kaliv.myths.persistence;

import com.kaliv.myths.model.artefacts.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
