package com.kaliv.myths.repositories;

import com.kaliv.myths.entities.artefacts.Poem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoemRepository extends JpaRepository<Poem, Long> {
}
