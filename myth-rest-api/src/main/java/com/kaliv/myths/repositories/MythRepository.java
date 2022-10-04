package com.kaliv.myths.repositories;

import com.kaliv.myths.entities.Myth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MythRepository extends JpaRepository<Myth, Long> {
}
