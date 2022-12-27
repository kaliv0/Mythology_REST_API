package com.kaliv.myths.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.TimePeriod;

public interface TimePeriodRepository extends JpaRepository<TimePeriod, Long> {
    Optional<TimePeriod> findByName(String name);
}
