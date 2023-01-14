package com.kaliv.myths.dto.musicDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MusicResponseDto extends BaseDto {
    private BaseDto Author;
    private BaseDto Myth;
    private String recordingUrl;
    private Set<BaseDto> mythCharacters;
}
