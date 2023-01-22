package com.kaliv.myths.dto.statueDtos;

import java.util.List;

import com.kaliv.myths.dto.PaginatedResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginatedStatueResponseDto extends PaginatedResponseDto {
    private List<StatueResponseDto> content;
}
