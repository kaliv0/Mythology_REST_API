package com.kaliv.myths.dto.poemDtos;

import javax.validation.constraints.Positive;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePoemDto {
    private String name;

    @Positive
    private Long AuthorId;

    @Positive
    private Long MythId;

    private String fullTextUrl;

    private String excerpt;

    private Set<@Positive Long> mythCharactersToAdd;

    private Set<@Positive Long> mythCharactersToRemove;
}
