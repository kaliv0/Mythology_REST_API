package com.kaliv.myths.dto.museumDtos;

import com.kaliv.myths.dto.artworkPosessorDto.UpdateVisualArtworkPossessorDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMuseumDto extends UpdateVisualArtworkPossessorDto {
    private String name;
}
