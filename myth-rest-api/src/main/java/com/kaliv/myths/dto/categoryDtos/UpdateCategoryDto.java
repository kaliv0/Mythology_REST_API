package com.kaliv.myths.dto.categoryDtos;

import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCategoryDto {
    private String name;
    private Collection<Long> mythCharactersToAdd;
    private Collection<Long> mythCharactersToRemove;
}
