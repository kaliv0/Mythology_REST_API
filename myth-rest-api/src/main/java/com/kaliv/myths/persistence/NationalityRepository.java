package com.kaliv.myths.persistence;

import com.kaliv.myths.model.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NationalityRepository extends JpaRepository<Nationality, Long> {
}
