package com.kaliv.myths.dto.mythDtos;

import java.util.Set;

import com.kaliv.myths.dto.artworkPosessorDto.ArtworkPossessorDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MythDto extends ArtworkPossessorDto {
    private String plot;
    private Long nationalityId;
    private Set<Long> mythCharacterIds;
}
