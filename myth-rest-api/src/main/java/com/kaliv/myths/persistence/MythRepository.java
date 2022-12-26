package com.kaliv.myths.persistence;

import java.util.Optional;

import com.kaliv.myths.entity.Myth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MythRepository extends JpaRepository<Myth, Long> {
    Optional<Myth> findByTitle(String title);
}
