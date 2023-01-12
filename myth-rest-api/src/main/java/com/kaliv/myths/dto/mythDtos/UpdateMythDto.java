package com.kaliv.myths.dto.mythDtos;

import java.util.Collection;

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
    private Collection<Long> mythCharactersToAdd;
    private Collection<Long> mythsCharactersToRemove;
}
