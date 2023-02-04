package com.kaliv.myths.dto.categoryDtos;

import javax.validation.constraints.Positive;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCategoryDto {
    private String name;
    private Set<@Positive Long> mythCharactersToAdd;
    private Set<@Positive Long> mythCharactersToRemove;
}
