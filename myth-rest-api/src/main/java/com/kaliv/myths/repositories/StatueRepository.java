package com.kaliv.myths.repositories;

import com.kaliv.myths.entities.artefacts.Statue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatueRepository extends JpaRepository<Statue, Long> {
}
