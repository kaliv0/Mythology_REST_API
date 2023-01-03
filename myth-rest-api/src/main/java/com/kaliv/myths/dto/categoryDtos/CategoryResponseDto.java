package com.kaliv.myths.dto.categoryDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryResponseDto extends BaseDto {
    private Set<BaseDto> mythCharacters;
}
