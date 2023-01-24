package com.kaliv.myths.dto.imageDtos;

import java.util.List;

import com.kaliv.myths.dto.PaginatedResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginatedImageResponseDto extends PaginatedResponseDto {
    private List<ImageDetailsDto> content;
}
