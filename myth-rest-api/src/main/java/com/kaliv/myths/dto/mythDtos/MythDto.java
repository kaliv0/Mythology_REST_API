package com.kaliv.myths.dto.mythDtos;

import com.kaliv.myths.model.Character;
import com.kaliv.myths.model.Nationality;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class MythDto {
    private long id;
    private String title;
    private String plot;
    private Nationality nationality;
    private Set<Character> characters = new HashSet<>();
}
