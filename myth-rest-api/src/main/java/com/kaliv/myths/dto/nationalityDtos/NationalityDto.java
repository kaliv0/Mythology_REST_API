package com.kaliv.myths.dto.nationalityDtos;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NationalityDto {
    private long id;
    private String name;
    private Set<Long> mythIds;
    private Set<Long> authorIds;
}
