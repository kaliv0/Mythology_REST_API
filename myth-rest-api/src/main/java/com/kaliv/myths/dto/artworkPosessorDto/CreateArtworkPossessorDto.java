package com.kaliv.myths.dto.artworkPosessorDto;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class CreateArtworkPossessorDto {
    private Set<Long> statueIds;
    private Set<Long> paintingIds;
    private Set<Long> musicIds;
    private Set<Long> poemIds;
}
