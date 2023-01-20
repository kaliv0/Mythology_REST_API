package com.kaliv.myths.service.statue;

import java.util.List;

import com.kaliv.myths.dto.statueDtos.CreateStatueDto;
import com.kaliv.myths.dto.statueDtos.StatueDto;
import com.kaliv.myths.dto.statueDtos.StatueResponseDto;
import com.kaliv.myths.dto.statueDtos.UpdateStatueDto;

public interface StatueService {
    List<StatueResponseDto> getAllStatues();

    StatueResponseDto getStatueById(long id);

    StatueDto createStatue(CreateStatueDto dto);

    StatueDto updateStatue(long id, UpdateStatueDto dto);

    void deleteStatue(long id);
}
