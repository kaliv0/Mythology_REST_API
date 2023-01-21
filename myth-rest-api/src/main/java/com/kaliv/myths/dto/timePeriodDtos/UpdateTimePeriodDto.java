package com.kaliv.myths.dto.timePeriodDtos;

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
    private Set<Long> authorsToAdd;
    private Set<Long> authorsToRemove;
}
