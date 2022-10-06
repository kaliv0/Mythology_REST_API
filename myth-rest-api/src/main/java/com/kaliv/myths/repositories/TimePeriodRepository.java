package com.kaliv.myths.repositories;

import com.kaliv.myths.entities.artefacts.TimePeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimePeriodRepository extends JpaRepository<TimePeriod, Long> {
}
