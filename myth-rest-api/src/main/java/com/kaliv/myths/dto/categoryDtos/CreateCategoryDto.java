package com.kaliv.myths.dto.categoryDtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateCategoryDto {
    @NotBlank
    private String name;

    private Set<@Positive Long> mythCharacterIds;
}
