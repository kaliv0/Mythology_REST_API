package com.kaliv.myths.dto.paintingDtos;

import java.util.List;

import com.kaliv.myths.dto.PaginatedResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginatedPaintingResponseDto extends PaginatedResponseDto {
    private List<PaintingResponseDto> content;
}
