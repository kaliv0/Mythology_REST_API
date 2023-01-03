package com.kaliv.myths.dto.mythDtos;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.entity.MythCharacter;
import com.kaliv.myths.entity.Nationality;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MythDto extends BaseDto {
    private String plot;
    private Nationality nationality;
    private Set<MythCharacter> mythCharacters = new HashSet<>();
}
