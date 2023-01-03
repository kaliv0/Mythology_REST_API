package com.kaliv.myths.dto.categoryDtos;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
    private long id;
    private String name;
    private Set<Long> mythCharacterIds;
}
