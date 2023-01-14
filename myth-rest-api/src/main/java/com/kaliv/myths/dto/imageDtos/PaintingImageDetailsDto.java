package com.kaliv.myths.dto.imageDtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class PaintingImageDetailsDto extends ImageDetailsDto {
    private Long paintingId;
}
