package com.kaliv.myths.dto.artworkPosessorDtos;

import javax.validation.constraints.Positive;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class CreateArtworkPossessorDto {
    private Set<@Positive Long> statueIds;
    private Set<@Positive Long> paintingIds;
    private Set<@Positive Long> musicIds;
    private Set<@Positive Long> poemIds;
}
