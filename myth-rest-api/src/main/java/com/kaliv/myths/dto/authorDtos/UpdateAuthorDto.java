package com.kaliv.myths.dto.authorDtos;

import javax.validation.constraints.Positive;

import com.kaliv.myths.dto.artworkPosessorDto.UpdateArtworkPossessorDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateAuthorDto extends UpdateArtworkPossessorDto {
    private String name;

    @Positive
    private Long timePeriodId;

    @Positive
    private Long nationalityId;
}
