package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.TimePeriod;

public interface TimePeriodRepository extends JpaRepository<TimePeriod, Long> {
    boolean existsByName(String name);
}
