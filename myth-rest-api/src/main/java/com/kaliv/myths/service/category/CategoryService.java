package com.kaliv.myths.service.category;

import java.util.List;

import com.kaliv.myths.dto.categoryDtos.CategoryDto;
import com.kaliv.myths.dto.categoryDtos.CategoryResponseDto;
import com.kaliv.myths.dto.categoryDtos.CreateCategoryDto;
import com.kaliv.myths.dto.categoryDtos.UpdateCategoryDto;

public interface CategoryService {
    List<CategoryResponseDto> getAllCategories();

    CategoryResponseDto getCategoryById(long id);

    CategoryDto createCategory(CreateCategoryDto dto);

    CategoryDto updateCategory(long id, UpdateCategoryDto dto);

    void deleteCategory(long id);
}
