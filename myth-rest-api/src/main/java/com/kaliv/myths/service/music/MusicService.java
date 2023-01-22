package com.kaliv.myths.service.music;

import com.kaliv.myths.common.criteria.PaginationCriteria;
import com.kaliv.myths.common.criteria.SortCriteria;
import com.kaliv.myths.dto.musicDtos.*;

public interface MusicService {
    PaginatedMusicResponseDto getAllMusic(String authorName,
                                          String mythName,
                                          String characterName,
                                          PaginationCriteria paginationCriteria,
                                          SortCriteria sortCriteria);

    MusicResponseDto getMusicById(long id);

    MusicDto createMusic(CreateMusicDto dto);

    MusicDto updateMusic(long id, UpdateMusicDto dto);

    void deleteMusic(long id);
}
