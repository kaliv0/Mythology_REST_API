package com.kaliv.myths.persistence;

import com.kaliv.myths.model.TimePeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimePeriodRepository extends JpaRepository<TimePeriod, Long> {
}
