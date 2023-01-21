package com.kaliv.myths.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.kaliv.myths.dto.BaseDto;
import com.kaliv.myths.dto.musicDtos.CreateMusicDto;
import com.kaliv.myths.dto.musicDtos.MusicDto;
import com.kaliv.myths.dto.musicDtos.MusicResponseDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Music;

public class MusicMapper {
    private final ModelMapper mapper;

    public MusicMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public MusicDto musicToDto(Music music) {
        MusicDto musicDto = mapper.map(music, MusicDto.class);
        musicDto.setMythCharacterIds(
                music.getMythCharacters().stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toSet()));
        return musicDto;
    }

    public MusicResponseDto musicToResponseDto(Music music) {
        MusicResponseDto musicResponseDto = mapper.map(music, MusicResponseDto.class);
        musicResponseDto.setMythCharacters(
                music.getMythCharacters().stream()
                        .map(character -> mapper.map(character, BaseDto.class))
                        .collect(Collectors.toSet()));
        return musicResponseDto;
    }

    public Music dtoToMusic(CreateMusicDto dto) {
        return mapper.map(dto, Music.class);
    }
}
