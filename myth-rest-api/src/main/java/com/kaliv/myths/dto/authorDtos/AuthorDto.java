package com.kaliv.myths.dto.authorDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorDto extends BaseDto {
    private Long timePeriodId;
    private Long nationalityId;
    private Set<Long> statueIds;
    private Set<Long> paintingIds;
    private Set<Long> musicIds;
    private Set<Long> poemIds;
}
