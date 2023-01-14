package com.kaliv.myths.dto.musicDtos;

import javax.validation.constraints.NotBlank;

import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMusicDto {
    @NotBlank
    private String name;
    private Long AuthorId;
    private Long MythId;
    private String recordingUrl;
    private Collection<Long> mythCharacterIds;
}
