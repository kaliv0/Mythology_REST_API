package com.kaliv.myths.dto.artworkPosessorDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class ArtworkPossessorDto extends BaseDto {
    private Set<Long> statueIds;
    private Set<Long> paintingIds;
    private Set<Long> musicIds;
    private Set<Long> poemIds;
}
