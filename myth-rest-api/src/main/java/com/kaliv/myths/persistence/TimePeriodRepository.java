package com.kaliv.myths.persistence;

import org.springframework.data.repository.CrudRepository;

import com.kaliv.myths.entity.TimePeriod;

public interface TimePeriodRepository extends CrudRepository<TimePeriod, Long> {
    boolean existsByName(String name);
}
