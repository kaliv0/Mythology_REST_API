package com.kaliv.myths.dto.musicDtos;

import java.util.List;

import com.kaliv.myths.dto.PaginatedResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginatedMusicResponseDto extends PaginatedResponseDto {
    private List<MusicResponseDto> content;
}
