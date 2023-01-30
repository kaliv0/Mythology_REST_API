package com.kaliv.myths.dto.authorDtos;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.artworkPosessorDto.ArtworkPossessorResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorResponseDto extends ArtworkPossessorResponseDto {
    private BaseDto timePeriod;
    private BaseDto nationality;
}
