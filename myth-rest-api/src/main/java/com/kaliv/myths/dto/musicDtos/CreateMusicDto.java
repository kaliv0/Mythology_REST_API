package com.kaliv.myths.dto.musicDtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMusicDto {
    @NotBlank
    private String name;

    @Positive
    private Long AuthorId;

    @Positive
    private Long MythId;

    private String recordingUrl;

    private Set<@Positive Long> mythCharacterIds;
}
