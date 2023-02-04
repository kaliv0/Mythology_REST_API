package com.kaliv.myths.dto.statueDtos;

import javax.validation.constraints.Positive;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateStatueDto {
    private String name;

    @Positive
    private Long AuthorId;

    @Positive
    private Long MythId;

    @Positive
    private Long MuseumId;

    private Set<@Positive Long> mythCharactersToAdd;

    private Set<@Positive Long> mythCharactersToRemove;

    private Set<@Positive Long> statueImagesToAdd;
    @Positive
    private Set<@Positive Long> statueImagesToRemove;
}
