package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.categoryDtos.CategoryDto;
import com.kaliv.myths.dto.categoryDtos.CategoryResponseDto;
import com.kaliv.myths.dto.categoryDtos.CreateCategoryDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Category;

public class CategoryMapper {
    private final ModelMapper mapper;

    public CategoryMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public CategoryDto categoryToDto(Category category) {
        CategoryDto categoryDto = mapper.map(category, CategoryDto.class);
        categoryDto.setMythCharacterIds(
                category.getMythCharacters().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return categoryDto;
    }

    public CategoryResponseDto categoryToResponseDto(Category category) {
        CategoryResponseDto categoryResponseDto = mapper.map(category, CategoryResponseDto.class);
        categoryResponseDto.setMythCharacters(
                category.getMythCharacters().stream()
                        .map(character -> mapper.map(character, BaseDto.class))
                        .collect(Collectors.toSet()));
        return categoryResponseDto;
    }

    public Category dtoToCategory(CreateCategoryDto dto) {
        return mapper.map(dto, Category.class);
    }
}
