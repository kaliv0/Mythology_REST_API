package com.kaliv.myths.service.statue;

import com.kaliv.myths.dto.statueDtos.*;

public interface StatueService {
    PaginatedStatueResponseDto getAllStatues(String authorName,
                                             String mythName,
                                             String museumName,
                                             String characterName,
                                             int pageNumber,
                                             int pageSize,
                                             String sortBy,
                                             String sortOrder);

    StatueResponseDto getStatueById(long id);

    StatueDto createStatue(CreateStatueDto dto);

    StatueDto updateStatue(long id, UpdateStatueDto dto);

    void deleteStatue(long id);
}
