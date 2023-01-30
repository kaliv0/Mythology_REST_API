package com.kaliv.myths.dto.mythDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MythResponseDto extends BaseDto {
    private String plot;
    private BaseDto nationality;
    private Set<BaseDto> mythCharacters;
    private Set<BaseDto> statues;
    private Set<BaseDto> paintings;
    private Set<BaseDto> music;
    private Set<BaseDto> poems;
}
