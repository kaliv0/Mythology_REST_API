package com.kaliv.myths.service.myth;

import com.kaliv.myths.common.PaginationCriteria;
import com.kaliv.myths.common.SortCriteria;
import com.kaliv.myths.dto.mythDtos.CreateMythDto;
import com.kaliv.myths.dto.mythDtos.MythDto;
import com.kaliv.myths.dto.mythDtos.MythResponseDto;
import com.kaliv.myths.dto.mythDtos.UpdateMythDto;

public interface MythService {
    MythResponseDto getAllMyths(PaginationCriteria paginationCriteria, SortCriteria sortCriteria);
//    MythResponseDto getAllMyths();

    MythDto getMythById(long id);

    MythDto createMyth(CreateMythDto dto);

    MythDto updateMyth(long id, UpdateMythDto dto);

    void deleteMyth(long id);
}
