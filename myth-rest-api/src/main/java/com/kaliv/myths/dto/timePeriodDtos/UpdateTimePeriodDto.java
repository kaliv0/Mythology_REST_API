package com.kaliv.myths.dto.timePeriodDtos;

import javax.validation.constraints.Positive;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTimePeriodDto {
    private String name;
    private String years;
    private Set<@Positive Long> authorsToAdd;
    private Set<@Positive Long> authorsToRemove;
}
