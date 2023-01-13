package com.kaliv.myths.service.poem;

import java.util.List;

import com.kaliv.myths.dto.poemDtos.CreatePoemDto;
import com.kaliv.myths.dto.poemDtos.PoemDto;
import com.kaliv.myths.dto.poemDtos.PoemResponseDto;
import com.kaliv.myths.dto.poemDtos.UpdatePoemDto;

public interface PoemService {
    List<PoemResponseDto> getAllPoems();

    PoemResponseDto getPoemById(long id);

    PoemDto createPoem(CreatePoemDto dto);

    PoemDto updatePoem(long id, UpdatePoemDto dto);

    void deletePoem(long id);
}
