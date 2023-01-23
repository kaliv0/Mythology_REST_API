package com.kaliv.myths.service.category;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.common.utils.Tuple;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.categoryDtos.CategoryDto;
import com.kaliv.myths.dto.categoryDtos.CategoryResponseDto;
import com.kaliv.myths.dto.categoryDtos.CreateCategoryDto;
import com.kaliv.myths.dto.categoryDtos.UpdateCategoryDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Category;
import com.kaliv.myths.entity.MythCharacter;
import com.kaliv.myths.exception.alreadyExists.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.invalidInput.DuplicateEntriesException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.CategoryMapper;
import com.kaliv.myths.persistence.CategoryRepository;
import com.kaliv.myths.persistence.MythCharacterRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MythCharacterRepository mythCharacterRepository;
    private final CategoryMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               MythCharacterRepository mythCharacterRepository,
                               CategoryMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mythCharacterRepository = mythCharacterRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream().map(mapper::categoryToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getCategoryById(long id) {
        Category categoryInDb = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.CATEGORY, Fields.ID, id));
        return mapper.categoryToResponseDto(categoryInDb);
    }

    @Override
    public CategoryDto createCategory(CreateCategoryDto dto) {
        String name = dto.getName();
        if (categoryRepository.existsByName(name)) {
            throw new ResourceWithGivenValuesExistsException(Sources.CATEGORY, Fields.NAME, name);
        }

        List<Long> mythCharacterIds = new ArrayList<>(dto.getMythCharacterIds());
        List<MythCharacter> mythCharacters = mythCharacterRepository.findAllById(mythCharacterIds);
        if (mythCharacters.size() != mythCharacterIds.size()) {
            throw new ResourceListNotFoundException(Sources.CHARACTERS, Fields.IDS);
        }

        Category category = mapper.dtoToCategory(dto);
        category.setMythCharacters(new HashSet<>(mythCharacters));
        Category savedCategory = categoryRepository.save(category);

//        mythCharacters.forEach(a -> a.setCategory(savedCategory));
//        mythCharacterRepository.saveAll(mythCharacters);
        return mapper.categoryToDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(long id, UpdateCategoryDto dto) {
        Category categoryInDb = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.CATEGORY, Fields.ID, id));

        if (Optional.ofNullable(dto.getName()).isPresent()) {
            String newName = dto.getName();
            if (!newName.equals(categoryInDb.getName()) && categoryRepository.existsByName(newName)) {
                throw new ResourceWithGivenValuesExistsException(Sources.CATEGORY, Fields.NAME, newName);
            }
            categoryInDb.setName(dto.getName());
        }

        Tuple<List<MythCharacter>, List<MythCharacter>> mythCharactersToUpdate = this.getValidMythCharacters(dto, categoryInDb);
        List<MythCharacter> mythCharactersToAdd = mythCharactersToUpdate.getFirst();
        List<MythCharacter> mythCharactersToRemove = mythCharactersToUpdate.getSecond();
        categoryInDb.getMythCharacters().addAll(new HashSet<>(mythCharactersToAdd));
        categoryInDb.getMythCharacters().removeAll(new HashSet<>(mythCharactersToRemove));
        categoryRepository.save(categoryInDb);

        mythCharactersToAdd.forEach(a -> a.setCategory(categoryInDb));
        mythCharacterRepository.saveAll(mythCharactersToAdd);
        mythCharactersToRemove.forEach(a -> a.setCategory(null));
        mythCharacterRepository.saveAll(mythCharactersToRemove);

        return mapper.categoryToDto(categoryInDb);
    }

    @Override
    public void deleteCategory(long id) {
        Category categoryInDb = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.CATEGORY, Fields.ID, id));
        categoryRepository.delete(categoryInDb);
    }

    private Tuple<List<MythCharacter>, List<MythCharacter>> getValidMythCharacters(UpdateCategoryDto dto, Category categoryInDb) {
        List<Long> mythCharactersToAddIds = new ArrayList<>(dto.getMythCharactersToAdd());
        List<Long> mythCharactersToRemoveIds = new ArrayList<>(dto.getMythCharactersToRemove());
        //check if user tries to add and remove same mythCharacter
        if (!Collections.disjoint(mythCharactersToAddIds, mythCharactersToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_CHARACTERS, Sources.REMOVE_CHARACTERS);
        }
        //check if user tries to add mythCharacter that is already in the list
        if (categoryInDb.getMythCharacters().stream()
                .map(BaseEntity::getId)
                .anyMatch(mythCharactersToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.CHARACTER);
        }
        //check if user tries to remove mythCharacter that is not in the list
        if (!categoryInDb.getMythCharacters().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet())
                .containsAll(mythCharactersToRemoveIds)) {
            throw new ResourceNotFoundException(Sources.CHARACTER);
        }

        List<MythCharacter> mythCharactersToAdd = mythCharacterRepository.findAllById(mythCharactersToAddIds);
        List<MythCharacter> mythCharactersToRemove = mythCharacterRepository.findAllById(mythCharactersToRemoveIds);
        if (mythCharactersToAddIds.size() != mythCharactersToAdd.size()
                || mythCharactersToRemoveIds.size() != mythCharactersToRemove.size()) {
            throw new ResourceListNotFoundException(Sources.CHARACTERS, Fields.IDS);
        }
        return new Tuple<>(mythCharactersToAdd, mythCharactersToRemove);
    }
}
