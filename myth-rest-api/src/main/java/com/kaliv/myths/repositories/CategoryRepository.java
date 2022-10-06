package com.kaliv.myths.repositories;

import com.kaliv.myths.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
