package com.kaliv.myths.service.painting;

import com.kaliv.myths.dto.paintingDtos.*;

public interface PaintingService {
    PaginatedPaintingResponseDto getAllPaintings(String authorName,
                                                 String mythName,
                                                 String museumName,
                                                 String characterName,
                                                 int pageNumber,
                                                 int pageSize,
                                                 String sortBy,
                                                 String sortOrder);

    PaintingResponseDto getPaintingById(long id);

    PaintingDto createPainting(CreatePaintingDto dto);

    PaintingDto updatePainting(long id, UpdatePaintingDto dto);

    void deletePainting(long id);
}
