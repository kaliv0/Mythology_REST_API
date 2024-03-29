package com.kaliv.myths.dto.mythDtos;

import java.util.List;

import com.kaliv.myths.dto.PaginatedResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginatedMythResponseDto extends PaginatedResponseDto {
    private List<MythResponseDto> content;
}
