package com.kaliv.myths.dto.paintingDtos;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePaintingDto {
    private String name;
    private Long AuthorId;
    private Long MythId;
    private Long MuseumId;
    private Set<Long> mythCharactersToAdd;
    private Set<Long> mythCharactersToRemove;
    private Set<Long> paintingImagesToAdd;
    private Set<Long> paintingImagesToRemove;
}
