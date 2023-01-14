package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.artefacts.Poem;

public interface PoemRepository extends JpaRepository<Poem, Long> {
    boolean existsByName(String name);
}
