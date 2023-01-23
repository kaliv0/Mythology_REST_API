package com.kaliv.myths.dto.mythCharacterDtos;

import java.util.List;

import com.kaliv.myths.dto.PaginatedResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginatedMythCharacterResponseDto extends PaginatedResponseDto {
    private List<MythCharacterResponseDto> content;
}
