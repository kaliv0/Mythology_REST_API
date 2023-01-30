package com.kaliv.myths.dto.artworkPosessorDto;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class ArtworkPossessorResponseDto extends BaseDto {
    private Set<BaseDto> statues;
    private Set<BaseDto> paintings;
    private Set<BaseDto> music;
    private Set<BaseDto> poems;
}
