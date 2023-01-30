package com.kaliv.myths.dto.artworkPosessorDto;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class UpdateArtworkPossessorDto {
    private Set<Long> statuesToAdd;
    private Set<Long> statuesToRemove;
    private Set<Long> paintingsToAdd;
    private Set<Long> paintingsToRemove;
    private Set<Long> musicToAdd;
    private Set<Long> musicToRemove;
    private Set<Long> poemsToAdd;
    private Set<Long> poemsToRemove;
}
