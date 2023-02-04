package com.kaliv.myths.dto.nationalityDtos;

import javax.validation.constraints.Positive;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateNationalityDto {
    private String name;
    private Set<@Positive Long> mythsToAdd;
    private Set<@Positive Long> mythsToRemove;
    private Set<@Positive Long> authorsToAdd;
    private Set<@Positive Long> authorsToRemove;
}
