package com.kaliv.myths.dto.mythCharacterDtos;

import javax.validation.constraints.NotBlank;

import java.util.Set;

import com.kaliv.myths.dto.artworkPosessorDto.CreateArtworkPossessorDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMythCharacterDto extends CreateArtworkPossessorDto {
    @NotBlank
    private String name;

    private Long categoryId;

    private Long fatherId;

    private Long motherId;

    private Set<Long> mythIds;
}
