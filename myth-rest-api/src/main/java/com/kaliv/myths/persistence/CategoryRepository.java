package com.kaliv.myths.persistence;

import org.springframework.data.repository.CrudRepository;

import com.kaliv.myths.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    boolean existsByName(String name);
}
