package com.kaliv.myths.service.poem;

import com.kaliv.myths.common.criteria.PaginationCriteria;
import com.kaliv.myths.common.criteria.SortCriteria;
import com.kaliv.myths.dto.poemDtos.*;

public interface PoemService {
    PaginatedPoemResponseDto getAllPoems(String authorName,
                                         String mythName,
                                         String characterName,
                                         PaginationCriteria paginationCriteria,
                                         SortCriteria sortCriteria);

    PoemResponseDto getPoemById(long id);

    PoemDto createPoem(CreatePoemDto dto);

    PoemDto updatePoem(long id, UpdatePoemDto dto);

    void deletePoem(long id);
}
