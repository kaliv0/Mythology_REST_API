package com.kaliv.myths.repositories;

import com.kaliv.myths.entities.God;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GodRepository extends JpaRepository<God, Long> {
}
