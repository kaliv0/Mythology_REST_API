package com.kaliv.myths.dto.authorDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorResponseDto extends BaseDto {
    private BaseDto timePeriod;
    private BaseDto nationality;
    private Set<BaseDto> statues;
    private Set<BaseDto> paintings;
    private Set<BaseDto> music;
    private Set<BaseDto> poems;
}
