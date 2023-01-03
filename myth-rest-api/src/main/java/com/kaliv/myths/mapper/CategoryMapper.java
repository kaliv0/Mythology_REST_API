package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.categoryDtos.CategoryDto;
import com.kaliv.myths.dto.categoryDtos.CategoryResponseDto;
import com.kaliv.myths.dto.categoryDtos.CreateCategoryDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Category;

@Component
public class CategoryMapper {
    private final GenericMapper mapper;

    public CategoryMapper(GenericMapper mapper) {
        this.mapper = mapper;
    }

    public CategoryDto categoryToDto(Category category) {
        CategoryDto categoryDto = mapper.entityToDto(category, CategoryDto.class);
        categoryDto.setMythCharacterIds(
                category.getMythCharacters().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return categoryDto;
    }

    public CategoryResponseDto categoryToResponseDto(Category category) {
        CategoryResponseDto categoryResponseDto = mapper.entityToDto(category, CategoryResponseDto.class);
        categoryResponseDto.setMythCharacters(mapper.mapNestedEntities(category.getMythCharacters(), BaseDto.class));
        return categoryResponseDto;
    }

    public Category dtoToCategory(CreateCategoryDto dto) {
        return mapper.dtoToEntity(dto, Category.class);
    }
}
