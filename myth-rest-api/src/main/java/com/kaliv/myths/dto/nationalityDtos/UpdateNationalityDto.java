package com.kaliv.myths.dto.nationalityDtos;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateNationalityDto {
    private String name;
    private Set<Long> mythsToAdd;
    private Set<Long> mythsToRemove;
    private Set<Long> authorsToAdd;
    private Set<Long> authorsToRemove;
}
