package com.kaliv.myths.repositories;

import com.kaliv.myths.entities.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NationalityRepository extends JpaRepository<Nationality, Long> {
}
