package com.kaliv.myths.persistence;

import com.kaliv.myths.entity.artefacts.Poem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoemRepository extends JpaRepository<Poem, Long> {
}
