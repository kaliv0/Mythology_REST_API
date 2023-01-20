package com.kaliv.myths.service.painting;

import java.util.List;

import com.kaliv.myths.dto.paintingDtos.CreatePaintingDto;
import com.kaliv.myths.dto.paintingDtos.PaintingDto;
import com.kaliv.myths.dto.paintingDtos.PaintingResponseDto;
import com.kaliv.myths.dto.paintingDtos.UpdatePaintingDto;

public interface PaintingService {
    List<PaintingResponseDto> getAllPaintings();

    PaintingResponseDto getPaintingById(long id);

    PaintingDto createPainting(CreatePaintingDto dto);

    PaintingDto updatePainting(long id, UpdatePaintingDto dto);

    void deletePainting(long id);
}
