package com.kaliv.myths.dto.paintingDtos;

import javax.validation.constraints.NotBlank;

import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatePaintingDto {
    @NotBlank
    private String name;
    private Long AuthorId;
    private Long MythId;
    private Long MuseumId;
    private Collection<Long> mythCharacterIds;
    private Collection<Long> paintingImageIds;
}
