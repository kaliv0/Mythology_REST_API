package com.kaliv.myths.dto.categoryDtos;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCategoryDto {
    private String name;
    private Set<Long> mythCharactersToAdd;
    private Set<Long> mythCharactersToRemove;
}
