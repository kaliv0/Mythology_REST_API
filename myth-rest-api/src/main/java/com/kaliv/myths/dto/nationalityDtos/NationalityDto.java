package com.kaliv.myths.dto.nationalityDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NationalityDto extends BaseDto {
    private Set<Long> mythIds;
    private Set<Long> authorIds;
}
