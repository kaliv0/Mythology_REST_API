package com.kaliv.myths.dto.nationalityDtos;

import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateNationalityDto {
    private String name;
    private Collection<Long> mythsToAdd;
    private Collection<Long> mythsToRemove;
    private Collection<Long> authorsToAdd;
    private Collection<Long> authorsToRemove;
}
