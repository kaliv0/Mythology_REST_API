package com.kaliv.myths.dto.mythDtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.model.Nationality;
import com.kaliv.myths.model.MythCharacter;

@Getter
@Setter
@NoArgsConstructor
public class MythDto {
    private long id;
    private String title;
    private String plot;
    private Nationality nationality;
    private Set<MythCharacter> mythCharacters = new HashSet<>();
}
