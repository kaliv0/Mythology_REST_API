package com.kaliv.myths.dto.poemDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PoemResponseDto extends BaseDto {
    private BaseDto Author;
    private BaseDto Myth;
    private String recordingUrl;
    private String excerpt;
    private Set<BaseDto> mythCharacters;
}
