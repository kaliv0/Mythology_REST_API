package com.kaliv.myths.dto.poemDtos;

import java.util.List;

import com.kaliv.myths.dto.PaginatedResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginatedPoemResponseDto extends PaginatedResponseDto {
    private List<PoemResponseDto> content;
}
