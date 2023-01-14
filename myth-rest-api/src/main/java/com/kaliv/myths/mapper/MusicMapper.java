package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.musicDtos.CreateMusicDto;
import com.kaliv.myths.dto.musicDtos.MusicDto;
import com.kaliv.myths.dto.musicDtos.MusicResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Music;

@Component
public class MusicMapper {
    private final GenericMapper mapper;

    public MusicMapper(GenericMapper mapper) {
        this.mapper = mapper;
    }

    public MusicDto musicToDto(Music music) {
        MusicDto musicDto = mapper.entityToDto(music, MusicDto.class);
        musicDto.setMythCharacterIds(
                music.getMythCharacters().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return musicDto;
    }

    public MusicResponseDto musicToResponseDto(Music music) {
        MusicResponseDto musicResponseDto = mapper.entityToDto(music, MusicResponseDto.class);
        musicResponseDto.setMythCharacters(mapper.mapNestedEntities(music.getMythCharacters(), BaseDto.class));
        return musicResponseDto;
    }

    public Music dtoToMusic(CreateMusicDto dto) {
        return mapper.dtoToEntity(dto, Music.class);
    }
}
