package com.kaliv.myths.persistence;

import com.kaliv.myths.model.Myth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MythRepository extends JpaRepository<Myth, Long> {
}
