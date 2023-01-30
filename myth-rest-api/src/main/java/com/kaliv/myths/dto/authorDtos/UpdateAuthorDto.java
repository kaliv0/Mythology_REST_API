package com.kaliv.myths.dto.authorDtos;

import com.kaliv.myths.dto.artworkPosessorDto.UpdateArtworkPossessorDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateAuthorDto extends UpdateArtworkPossessorDto {
    private String name;
    private Long timePeriodId;
    private Long nationalityId;
}
