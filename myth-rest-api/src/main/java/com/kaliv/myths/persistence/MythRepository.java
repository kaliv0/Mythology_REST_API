package com.kaliv.myths.persistence;

import org.springframework.data.repository.CrudRepository;

import com.kaliv.myths.entity.Myth;

public interface MythRepository extends CrudRepository<Myth, Long> {
    boolean existsByName(String name);
}
