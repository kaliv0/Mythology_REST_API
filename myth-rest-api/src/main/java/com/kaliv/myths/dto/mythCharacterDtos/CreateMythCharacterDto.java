package com.kaliv.myths.dto.mythCharacterDtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import java.util.Set;

import com.kaliv.myths.dto.artworkPosessorDtos.CreateArtworkPossessorDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMythCharacterDto extends CreateArtworkPossessorDto {
    @NotBlank
    private String name;

    @Positive
    private Long categoryId;

    @Positive
    private Long fatherId;

    @Positive
    private Long motherId;


    private Set<@Positive Long> mythIds;
}
