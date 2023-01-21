package com.kaliv.myths.dto.mythCharacterDtos;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMythCharacterDto {
    private String name;
    private Long categoryId;
    private Long fatherId;
    private Long motherId;
    private Set<Long> mythsToAdd;
    private Set<Long> mythsToRemove;
}
