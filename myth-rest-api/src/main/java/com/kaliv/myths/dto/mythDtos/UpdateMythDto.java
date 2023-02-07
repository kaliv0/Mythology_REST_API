package com.kaliv.myths.dto.mythDtos;

import javax.validation.constraints.Positive;

import java.util.Set;

import com.kaliv.myths.dto.artworkPosessorDtos.UpdateArtworkPossessorDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMythDto extends UpdateArtworkPossessorDto {
    private String name;

    private String plot;

    @Positive
    private Long nationalityId;

    private Set<@Positive Long> mythCharactersToAdd;

    private Set<@Positive Long> mythsCharactersToRemove;
}
