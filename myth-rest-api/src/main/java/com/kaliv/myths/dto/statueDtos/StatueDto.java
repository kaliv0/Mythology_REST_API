package com.kaliv.myths.dto.statueDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatueDto extends BaseDto {
    private Long AuthorId;
    private Long MythId;
    private Long MuseumId;
    private Set<Long> mythCharacterIds;
    private Set<Long> statueImageIds;
}
