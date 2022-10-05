package com.kaliv.myths.services.contracts;

import com.kaliv.myths.common.PaginationCriteria;
import com.kaliv.myths.common.SortCriteria;
import com.kaliv.myths.dtos.mythsDtos.CreateMythDto;
import com.kaliv.myths.dtos.mythsDtos.MythDto;
import com.kaliv.myths.dtos.mythsDtos.MythResponseDto;
import com.kaliv.myths.dtos.mythsDtos.UpdateMythDto;

public interface MythService {
    MythResponseDto getAllMyths(PaginationCriteria paginationCriteria, SortCriteria sortCriteria);

    MythDto getMythById(long id);

    MythDto createMyth(CreateMythDto dto);

    MythDto updateMyth(long id, UpdateMythDto dto);

    void deleteMyth(long id);
}
