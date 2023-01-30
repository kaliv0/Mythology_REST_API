package com.kaliv.myths.dto.mythDtos;

import javax.validation.constraints.NotBlank;

import java.util.Set;

import com.kaliv.myths.dto.artworkPosessorDto.CreateArtworkPossessorDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMythDto extends CreateArtworkPossessorDto {
    @NotBlank
    private String name;

    @NotBlank
    private String plot;

    private Long nationalityId;

    private Set<Long> mythCharacterIds;
}
