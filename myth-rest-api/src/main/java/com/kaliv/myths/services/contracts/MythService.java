package com.kaliv.myths.services.contracts;

import com.kaliv.myths.dtos.mythsDtos.CreateMythDto;
import com.kaliv.myths.dtos.mythsDtos.MythDto;

import java.util.List;

public interface MythService {
    List<MythDto> getAllMyths();

    MythDto getMythById(long id);

    MythDto createMyth(CreateMythDto dto);

    void deleteMyth(long id);
}
