package com.kaliv.myths.dtos.mythDtos;

import com.kaliv.myths.entities.Character;
import com.kaliv.myths.entities.Nationality;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class MythDto {
    private long id;
    private String title;
    private String plot;
    private Nationality nationality;
    private Set<Character> characters = new HashSet<>();
}
