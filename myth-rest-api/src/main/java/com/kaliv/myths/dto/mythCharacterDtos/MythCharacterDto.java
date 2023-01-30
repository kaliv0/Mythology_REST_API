package com.kaliv.myths.dto.mythCharacterDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MythCharacterDto extends BaseDto {
    private Long categoryId;
    private Long fatherId;
    private Long motherId;
    private Set<Long> mythIds;
    private Set<Long> statueIds;
    private Set<Long> paintingIds;
    private Set<Long> musicIds;
    private Set<Long> poemIds;
}
