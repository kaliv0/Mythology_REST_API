package com.kaliv.myths.dto.musicDtos;

import java.util.Set;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MusicDto extends BaseDto {
    private Long AuthorId;
    private Long MythId;
    private String recordingUrl;
    private Set<Long> mythCharacterIds;
}
