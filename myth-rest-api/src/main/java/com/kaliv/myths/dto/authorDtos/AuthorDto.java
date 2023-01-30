package com.kaliv.myths.dto.authorDtos;

import com.kaliv.myths.dto.artworkPosessorDto.ArtworkPossessorDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorDto extends ArtworkPossessorDto {
    private Long timePeriodId;
    private Long nationalityId;
}
