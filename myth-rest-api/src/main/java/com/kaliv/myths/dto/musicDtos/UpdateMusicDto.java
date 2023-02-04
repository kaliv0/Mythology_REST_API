package com.kaliv.myths.dto.musicDtos;

import javax.validation.constraints.Positive;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMusicDto {
    private String name;

    @Positive
    private Long AuthorId;

    @Positive
    private Long MythId;

    @Positive
    private String recordingUrl;

    private Set<@Positive Long> mythCharactersToAdd;

    private Set<@Positive Long> mythCharactersToRemove;
}
