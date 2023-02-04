package com.kaliv.myths.dto.paintingDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaintingDto extends BaseDto {
    private Long AuthorId;
    private Long MythId;
    private Long MuseumId;
    private Set<Long> mythCharacterIds;
    private Set<Long> paintingImageIds;
    private Set<Long> smallPaintingImageIds;
}
