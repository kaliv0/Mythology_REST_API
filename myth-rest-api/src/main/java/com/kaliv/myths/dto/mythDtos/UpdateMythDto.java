package com.kaliv.myths.dto.mythDtos;

import java.util.Set;

import com.kaliv.myths.dto.artworkPosessorDto.UpdateArtworkPossessorDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMythDto extends UpdateArtworkPossessorDto {
    private String name;
    private String plot;
    private Long nationalityId;
    private Set<Long> mythCharactersToAdd;
    private Set<Long> mythsCharactersToRemove;
}
