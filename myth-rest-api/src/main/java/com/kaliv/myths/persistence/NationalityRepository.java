package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.Nationality;

public interface NationalityRepository extends JpaRepository<Nationality, Long> {
    boolean existsByName(String name);
}
