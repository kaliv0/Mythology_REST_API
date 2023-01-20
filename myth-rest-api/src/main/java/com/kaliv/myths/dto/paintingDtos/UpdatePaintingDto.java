package com.kaliv.myths.dto.paintingDtos;

import java.util.Collection;

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
    private Collection<Long> mythCharactersToAdd;
    private Collection<Long> mythCharactersToRemove;
    private Collection<Long> paintingImagesToAdd;
    private Collection<Long> paintingImagesToRemove;
}
