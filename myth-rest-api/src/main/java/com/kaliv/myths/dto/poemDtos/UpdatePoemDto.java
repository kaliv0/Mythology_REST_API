package com.kaliv.myths.dto.poemDtos;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePoemDto {
    private String name;
    private Long AuthorId;
    private Long MythId;
    private String fullTextUrl;
    private String excerpt;
    private Set<Long> mythCharactersToAdd;
    private Set<Long> mythCharactersToRemove;
}
