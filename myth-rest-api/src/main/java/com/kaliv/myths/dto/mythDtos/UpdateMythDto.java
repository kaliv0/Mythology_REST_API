package com.kaliv.myths.dto.mythDtos;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMythDto {
    private String name;
    private String plot;
    private Long nationalityId;
    private Set<Long> mythCharactersToAdd;
    private Set<Long> mythsCharactersToRemove;
}
