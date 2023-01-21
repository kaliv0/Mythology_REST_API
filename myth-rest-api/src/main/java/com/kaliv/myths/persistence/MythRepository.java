package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.Myth;

public interface MythRepository extends JpaRepository<Myth, Long> {
    boolean existsByName(String name);
}
