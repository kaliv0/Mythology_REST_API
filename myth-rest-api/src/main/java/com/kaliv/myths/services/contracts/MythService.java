package com.kaliv.myths.services.contracts;

import com.kaliv.myths.dtos.mythsDtos.GetMythDto;

import java.util.List;

public interface MythService {
    List<GetMythDto> getAllMyths();

    GetMythDto getMythById(long id);

    void deleteMyth(long id);
}
