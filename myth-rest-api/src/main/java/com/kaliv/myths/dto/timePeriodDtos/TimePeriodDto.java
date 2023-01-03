package com.kaliv.myths.dto.timePeriodDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimePeriodDto extends BaseDto {
    private String years;
    private Set<Long> authorIds;
}
