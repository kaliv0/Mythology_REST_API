package com.kaliv.myths.persistence;

import org.springframework.data.repository.CrudRepository;

import com.kaliv.myths.entity.artefacts.Statue;

public interface StatueRepository extends CrudRepository<Statue, Long> {
    boolean existsByName(String name);
}
