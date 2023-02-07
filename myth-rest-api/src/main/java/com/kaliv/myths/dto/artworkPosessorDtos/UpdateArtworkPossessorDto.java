package com.kaliv.myths.dto.artworkPosessorDtos;

import javax.validation.constraints.Positive;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class UpdateArtworkPossessorDto extends UpdateVisualArtworkPossessorDto {
    private Set<@Positive Long> musicToAdd;
    private Set<@Positive Long> musicToRemove;
    private Set<@Positive Long> poemsToAdd;
    private Set<@Positive Long> poemsToRemove;
}
