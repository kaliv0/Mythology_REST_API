package com.kaliv.myths.dto.mythCharacterDtos;

import java.util.Set;

import com.kaliv.myths.dto.artworkPosessorDtos.ArtworkPossessorDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MythCharacterDto extends ArtworkPossessorDto {
    private Long categoryId;
    private Long fatherId;
    private Long motherId;
    private Set<Long> mythIds;
}
