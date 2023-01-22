package com.kaliv.myths.service.painting;

import com.kaliv.myths.common.criteria.PaginationCriteria;
import com.kaliv.myths.common.criteria.SortCriteria;
import com.kaliv.myths.dto.paintingDtos.*;

public interface PaintingService {
    PaginatedPaintingResponseDto getAllPaintings(String authorName,
                                                 String mythName,
                                                 String museumName,
                                                 String characterName,
                                                 PaginationCriteria paginationCriteria,
                                                 SortCriteria sortCriteria);

    PaintingResponseDto getPaintingById(long id);

    PaintingDto createPainting(CreatePaintingDto dto);

    PaintingDto updatePainting(long id, UpdatePaintingDto dto);

    void deletePainting(long id);
}
