package com.kaliv.myths.persistence;

import org.springframework.data.repository.CrudRepository;

import com.kaliv.myths.entity.Nationality;

public interface NationalityRepository extends CrudRepository<Nationality, Long> {
    boolean existsByName(String name);
}
