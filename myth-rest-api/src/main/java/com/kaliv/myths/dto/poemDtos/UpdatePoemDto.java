package com.kaliv.myths.dto.poemDtos;

import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePoemDto {
    private String name;
    private Long AuthorId;
    private Long MythId;
    private String fullTextUrl;
    private String excerpt;
    private Collection<Long> mythCharactersToAdd;
    private Collection<Long> mythCharactersToRemove;
}
