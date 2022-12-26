package com.kaliv.myths.persistence;

import com.kaliv.myths.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
