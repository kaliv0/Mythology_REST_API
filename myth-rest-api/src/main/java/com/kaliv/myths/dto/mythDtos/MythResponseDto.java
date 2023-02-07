package com.kaliv.myths.dto.mythDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.artworkPosessorDtos.ArtworkPossessorResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MythResponseDto extends ArtworkPossessorResponseDto {
    private String plot;
    private BaseDto nationality;
    private Set<BaseDto> mythCharacters;
}
