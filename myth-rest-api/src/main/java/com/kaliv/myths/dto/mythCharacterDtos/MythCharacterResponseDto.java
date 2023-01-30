package com.kaliv.myths.dto.mythCharacterDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.artworkPosessorDto.ArtworkPossessorResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MythCharacterResponseDto extends ArtworkPossessorResponseDto {
    private BaseDto category;
    private BaseDto father;
    private BaseDto mother;
    private Set<BaseDto> myths;
}
