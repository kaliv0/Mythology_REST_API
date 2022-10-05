package com.kaliv.myths.services.contracts;

import com.kaliv.myths.common.PaginationCriteria;
import com.kaliv.myths.common.SortCriteria;
import com.kaliv.myths.dtos.mythDtos.CreateMythDto;
import com.kaliv.myths.dtos.mythDtos.MythDto;
import com.kaliv.myths.dtos.mythDtos.MythResponseDto;
import com.kaliv.myths.dtos.mythDtos.UpdateMythDto;

public interface MythService {
    MythResponseDto getAllMyths(PaginationCriteria paginationCriteria, SortCriteria sortCriteria);

    MythDto getMythById(long id);

    MythDto createMyth(CreateMythDto dto);

    MythDto updateMyth(long id, UpdateMythDto dto);

    void deleteMyth(long id);
}
