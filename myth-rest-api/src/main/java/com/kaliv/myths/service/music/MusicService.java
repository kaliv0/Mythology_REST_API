package com.kaliv.myths.service.music;

import java.util.List;

import com.kaliv.myths.dto.musicDtos.CreateMusicDto;
import com.kaliv.myths.dto.musicDtos.MusicDto;
import com.kaliv.myths.dto.musicDtos.MusicResponseDto;
import com.kaliv.myths.dto.musicDtos.UpdateMusicDto;

public interface MusicService {
    List<MusicResponseDto> getAllMusic();

    MusicResponseDto getMusicById(long id);

    MusicDto createMusic(CreateMusicDto dto);

    MusicDto updateMusic(long id, UpdateMusicDto dto);

    void deleteMusic(long id);
}
