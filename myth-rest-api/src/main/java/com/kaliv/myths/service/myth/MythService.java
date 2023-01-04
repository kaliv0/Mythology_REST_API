package com.kaliv.myths.service.myth;

import com.kaliv.myths.common.criteria.PaginationCriteria;
import com.kaliv.myths.common.criteria.SortCriteria;
import com.kaliv.myths.dto.mythDtos.CreateUpdateMythDto;
import com.kaliv.myths.dto.mythDtos.MythDto;
import com.kaliv.myths.dto.mythDtos.MythResponseDto;

public interface MythService {
    MythResponseDto getAllMyths(PaginationCriteria paginationCriteria, SortCriteria sortCriteria);

    MythDto getMythById(long id);

    MythDto createMyth(CreateUpdateMythDto dto);

    MythDto updateMyth(long id, CreateUpdateMythDto dto);

    void deleteMyth(long id);
}
