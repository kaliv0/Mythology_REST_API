package com.kaliv.myths.dto.mythDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MythDto extends BaseDto {
    private String plot;
    private Long nationalityId;
    private Set<Long> mythCharacterIds;
}
