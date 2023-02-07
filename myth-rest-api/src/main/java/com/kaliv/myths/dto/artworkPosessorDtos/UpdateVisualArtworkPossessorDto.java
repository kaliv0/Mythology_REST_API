package com.kaliv.myths.dto.artworkPosessorDtos;

import javax.validation.constraints.Positive;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class UpdateVisualArtworkPossessorDto {
    private Set<@Positive Long> statuesToAdd;
    private Set<@Positive Long> statuesToRemove;
    private Set<@Positive Long> paintingsToAdd;
    private Set<@Positive Long> paintingsToRemove;
}
