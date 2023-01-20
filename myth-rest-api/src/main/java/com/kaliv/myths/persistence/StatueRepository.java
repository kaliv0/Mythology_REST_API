package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.artefacts.Statue;

public interface StatueRepository extends JpaRepository<Statue, Long> {
    boolean existsByName(String name);
}
