package com.kaliv.myths.dto.authorDtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import com.kaliv.myths.dto.artworkPosessorDto.CreateArtworkPossessorDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateAuthorDto extends CreateArtworkPossessorDto {
    @NotEmpty
    private String name;

    @Positive
    private Long timePeriodId;

    @Positive
    private Long nationalityId;
}
