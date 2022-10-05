package com.kaliv.myths.dtos.mythsDtos;

import com.kaliv.myths.dtos.baseDtos.BaseDto;
import com.kaliv.myths.entities.Character;
import com.kaliv.myths.entities.Nationality;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class GetMythDto extends BaseDto {
    private String plot;
    private Nationality nationality;
    private Set<Character> characters = new HashSet<>();
}
