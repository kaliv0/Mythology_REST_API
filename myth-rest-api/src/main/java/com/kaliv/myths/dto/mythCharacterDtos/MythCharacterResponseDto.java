package com.kaliv.myths.dto.mythCharacterDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MythCharacterResponseDto extends BaseDto {
    private BaseDto category;
    private BaseDto father;
    private BaseDto mother;
    private Set<BaseDto> myths;
}
