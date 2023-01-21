package com.kaliv.myths.persistence;

import org.springframework.data.repository.CrudRepository;

import com.kaliv.myths.entity.artefacts.Poem;

public interface PoemRepository extends CrudRepository<Poem, Long> {
    boolean existsByName(String name);
}
