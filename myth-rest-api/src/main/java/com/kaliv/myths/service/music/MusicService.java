package com.kaliv.myths.service.music;

import com.kaliv.myths.dto.musicDtos.*;

public interface MusicService {
    PaginatedMusicResponseDto getAllMusic(String authorName,
                                          String mythName,
                                          String characterName,
                                          int pageNumber,
                                          int pageSize,
                                          String sortBy,
                                          String sortOrder);

    MusicResponseDto getMusicById(long id);

    MusicDto createMusic(CreateMusicDto dto);

    MusicDto updateMusic(long id, UpdateMusicDto dto);

    void deleteMusic(long id);
}
