package com.kaliv.myths.dto.timePeriodDtos;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimePeriodDto {
    private long id;
    private String name;
    private String years;
    private Set<Long> authorIds;
}
