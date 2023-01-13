package com.kaliv.myths.dto.poemDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PoemDto extends BaseDto {
    private Long AuthorId;
    private Long MythId;
    private String fullTextUrl;
    private String excerpt;
    private Set<Long> mythCharacterIds;
}
