package com.kaliv.myths.dto.mythCharacterDtos;

import java.util.Set;

import com.kaliv.myths.dto.artworkPosessorDto.UpdateArtworkPossessorDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMythCharacterDto extends UpdateArtworkPossessorDto {
    private String name;
    private Long categoryId;
    private Long fatherId;
    private Long motherId;
    private Set<Long> mythsToAdd;
    private Set<Long> mythsToRemove;
}
