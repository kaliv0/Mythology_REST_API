package com.kaliv.myths.controller;

import javax.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.ResponseMessages;
import com.kaliv.myths.dto.categoryDtos.CategoryDto;
import com.kaliv.myths.dto.categoryDtos.CategoryResponseDto;
import com.kaliv.myths.dto.categoryDtos.CreateCategoryDto;
import com.kaliv.myths.dto.categoryDtos.UpdateCategoryDto;
import com.kaliv.myths.service.category.CategoryService;

@RestController
@RequestMapping("/api/v1/character-categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryDto dto) {
        return new ResponseEntity<>(categoryService.createCategory(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable("id") long id, @Valid @RequestBody UpdateCategoryDto dto) {
        return categoryService.updateCategory(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable(name = "id") long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(ResponseMessages.CATEGORY_DELETED, HttpStatus.OK);
    }
}