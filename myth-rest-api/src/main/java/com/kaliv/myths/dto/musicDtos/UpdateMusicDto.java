package com.kaliv.myths.dto.musicDtos;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMusicDto {
    private String name;
    private Long AuthorId;
    private Long MythId;
    private String recordingUrl;
    private Set<Long> mythCharactersToAdd;
    private Set<Long> mythCharactersToRemove;
}
