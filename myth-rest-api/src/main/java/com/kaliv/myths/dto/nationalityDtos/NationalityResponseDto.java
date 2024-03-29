package com.kaliv.myths.dto.nationalityDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NationalityResponseDto extends BaseDto {
    private Set<BaseDto> myths;
    private Set<BaseDto> authors;
}
